import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.CartDTO;
import model.ProductDTO;
import org.testng.annotations.Test;
import utils.TemplateBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static java.net.HttpURLConnection.HTTP_OK;
import static utils.Storage.rememberThatThe;
import static utils.Storage.whatIsThe;


public class RestAssuredAddToCardTest {
    private final TemplateBuilder templateBuilder = new TemplateBuilder();

    private static final String BASE_URL = "https://www.kruidvat.nl";
    private static final String CODE = "code";
    private static final String QUANTITY = "quantity";

    public static RequestSpecification requestSpecification() {
        return new RequestSpecBuilder()
                .setBaseUri(BASE_URL)
                .setContentType(ContentType.JSON)
                .build();
    }

    @Test
    public void verifyProductIsAddedToCardFromTemplate() throws IOException {
        createCart();
        addProductToCart();
    }

    @Test
    public void verifyProductIsAddedToCardFromBojo() throws IOException {
        createCart();
        addProductToCartWithModelPayload();
    }

    private void createCart() {
        Response response = given().spec(requestSpecification())
                .post("/api/v2/kvn/users/anonymous/carts");
        String guid = response.jsonPath().get("guid");
        String code = response.jsonPath().get("code");
        response.then().assertThat().statusCode(201);
        rememberThatThe("guid", guid);
        rememberThatThe("code", code);
    }

    private void addProductToCart() throws IOException {
        final Map<String, Object> givenValues = new HashMap<>();
        givenValues.put(CODE, "2876350");
        givenValues.put(QUANTITY, "1");
        String payload = templateBuilder.getResolvedTemplate("json.models/add_to_card.json", givenValues);
        Response response = given().spec(requestSpecification()).body(payload)
                .when().post(getAddToCartEndpoint());
        response.then().assertThat().statusCode(HTTP_OK);
    }

    private void addProductToCartWithModelPayload() {
        CartDTO cartPayload = new CartDTO();
        cartPayload.setQuantity("1");
        ProductDTO productPayload = new ProductDTO();
        productPayload.setCode("234");
        cartPayload.setProductDTO(productPayload);
        Response response = given().spec(requestSpecification()).body(cartPayload)
                .when().post(getAddToCartEndpoint());
        ///api/v2/kvn/users/anonymous/cartsOptional[404c8055-26a0-4d3d-b251-9044d8482b64]/entries
        response.then().assertThat().statusCode(200)
                .and().log().all().extract().body().jsonPath().get("data.code");
    }

    private String getAddToCartEndpoint() {
        return "/api/v2/kvn/users/anonymous/carts/" + whatIsThe("guid") + "/entries";
    }
}

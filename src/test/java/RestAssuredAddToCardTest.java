import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import model.CartDTO;
import model.ProductDTO;
import org.testng.Assert;
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
    public void verifyProductIsAddedToCardFromBojo() {
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
        String expectedCodeValue = "2876350";
        Integer expectedQuantityValue = 1;
        CartDTO cartPayload = new CartDTO();
        cartPayload.setQuantity(1);
        ProductDTO productPayload = new ProductDTO();
        productPayload.setCode("2876350");
        cartPayload.setProduct(productPayload);
        Response response = given().spec(requestSpecification()).body(cartPayload)
                .when().post(getAddToCartEndpoint());
        response.then().assertThat().statusCode(200);
        Assert.assertEquals(expectedQuantityValue, response.jsonPath().get("entry.quantity"));
        Assert.assertEquals(expectedCodeValue, response.jsonPath().get("entry.product.code"));
    }

    private String getAddToCartEndpoint() {
        return "/api/v2/kvn/users/anonymous/carts/" + whatIsThe("guid") + "/entries";
    }
}

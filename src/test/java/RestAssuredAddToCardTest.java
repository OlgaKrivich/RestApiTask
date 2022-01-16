import api.model.Cart;
import api.model.Product;
import api.utils.ModelBuilder;
import api.utils.TemplateUtils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.desktop.fragments.CruidCookiesFragment;
import ui.desktop.pages.CartPage;
import ui.driver.SingletonDriver;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static api.utils.Storage.*;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.net.HttpURLConnection.HTTP_OK;
import static ui.driver.WebDriverUtils.refreshPage;


public class RestAssuredAddToCardTest {
    private final TemplateUtils templateUtils = new TemplateUtils();
    private final CartPage cartPage = new CartPage(SingletonDriver.getDriver());
    private final CruidCookiesFragment cruidCookiesFragment = new CruidCookiesFragment(SingletonDriver.getDriver());


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
        cartPage.openCartPage();
        setAuthHeaders();
        refreshPage();
        cruidCookiesFragment.clickAcceptCookiesButton();
        cruidCookiesFragment.clickFranceButton();
    }

    @Test
    public void verifyProductIsAddedToCardFromBojo() {
        createCart();
        addProductToCartWithModelPayload();
        cartPage.openCartPage();
        setAuthHeaders();
        refreshPage();
        cruidCookiesFragment.clickAcceptCookiesButton();
        cruidCookiesFragment.clickFranceButton();
    }

    private void createCart() {
        Response cartResponse = given().spec(requestSpecification())
                .post("/api/v2/kvn/users/anonymous/carts");
        String guid = cartResponse.jsonPath().get("guid");
        String code = cartResponse.jsonPath().get("code");
        cartResponse.then().assertThat().statusCode(201)
                .and().assertThat().body(matchesJsonSchemaInClasspath("json.responses/create_cart_response_schema.json"));
        rememberThatThe("guid", guid);
        rememberThatThe("code", code);
    }

    private void addProductToCart() throws IOException {
        final Map<String, Object> givenValues = new HashMap<>();
        givenValues.put(CODE, "2876350");
        givenValues.put(QUANTITY, "1");
        String payload = templateUtils.getResolvedTemplate("json.models/add_to_card.json", givenValues);
        Response response = given().spec(requestSpecification()).body(payload)
                .when().post(getAddToCartEndpoint());
        response.then().assertThat().statusCode(HTTP_OK);
    }

    private void addProductToCartWithModelPayload() {
        String expectedCodeValue = "2876350";
        Integer expectedQuantityValue = 1;

        Product product = ModelBuilder.of(Product::new).with(Product::setCode, "2876350").build();
        Cart cartPayload = ModelBuilder.of(Cart::new).with(Cart::setProduct, product).with(Cart::setQuantity, 1).build();
//        Cart cartPayload = new Cart();
//        cartPayload.setQuantity(1);
//        Product productPayload = new Product();
//        productPayload.setCode("2876350");
//        cartPayload.setProduct(productPayload);
        Response response = given().spec(requestSpecification()).body(cartPayload)
                .when().post(getAddToCartEndpoint());
        response.then().assertThat().statusCode(200);
        Assert.assertEquals(expectedQuantityValue, response.jsonPath().get("entry.quantity"));
        Assert.assertEquals(expectedCodeValue, response.jsonPath().get("entry.product.code"));
    }

    private String getAddToCartEndpoint() {
        return "/api/v2/kvn/users/anonymous/carts/" + whatIsThe("guid") + "/entries";
    }

    private void setAuthHeaders() {
        SingletonDriver.getDriver().manage().deleteAllCookies();
        SingletonDriver.getDriver().manage().addCookie(new Cookie("kvn-cart",
                getInstance().whatIsThe("guid").toString()));
    }
}

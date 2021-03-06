import static api.utils.Storage.rememberThatThe;
import static api.utils.Storage.whatIsThe;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static java.net.HttpURLConnection.HTTP_OK;
import static ui.abstractClasses.AbstractPage.BASE_URL;
import static ui.constants.Constants.CODE;
import static ui.constants.Constants.QUANTITY;
import static ui.driver.WebDriverUtils.refreshPage;
import static ui.hooks.DriverHooks.setAuthHeaders;

import api.model.Cart;
import api.model.Product;
import api.utils.TemplateUtils;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import ui.desktop.fragments.CruidCookiesFragment;
import ui.desktop.pages.CartPage;
import ui.driver.SingletonDriver;


public class RestAssuredAddToCardTest {

    private final static Logger logger = Logger.getLogger(RestAssuredAddToCardTest.class);
    private final TemplateUtils templateUtils = new TemplateUtils();
    private final CartPage cartPage = new CartPage(SingletonDriver.getDriver());
    private final CruidCookiesFragment cruidCookiesFragment = new CruidCookiesFragment(
            SingletonDriver.getDriver());

    @Test
    public void performSomeTask() {
        logger.debug("This is a debug message");
        logger.info("This is an info message");
        logger.warn("This is a warn message");
        logger.error("This is an error message");
        logger.fatal("This is a fatal message");
    }

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
        cartPage.verifyProductName();
    }

    @Test
    public void verifyProductIsAddedToCardFromPojo() {
        createCart();
        addProductToCartWithModelPayload();
        cartPage.openCartPage();
        setAuthHeaders();
        refreshPage();
        cruidCookiesFragment.clickAcceptCookiesButton();
        cruidCookiesFragment.clickFranceButton();
        cartPage.verifyProductName();
    }

    private void createCart() {
        Response cartResponse = given().spec(requestSpecification())
                .post("/api/v2/kvn/users/anonymous/carts");
        String guid = cartResponse.jsonPath().get("guid");
        String code = cartResponse.jsonPath().get("code");
        cartResponse.then().assertThat().statusCode(201)
                .and().assertThat().body(matchesJsonSchemaInClasspath(
                "json.responses/create_cart_response_schema.json"));
        logger.info("The cart is created successfully");
        rememberThatThe("guid", guid);
        rememberThatThe("code", code);

    }

    private void addProductToCart() {
        final Map<String, Object> givenValues = new HashMap<>();
        givenValues.put(CODE, "2876350");
        givenValues.put(QUANTITY, "1");
        try {
            String payload = templateUtils
                    .getResolvedTemplate("json.models/add_to_card.json", givenValues);
            Response response = given().spec(requestSpecification()).body(payload)
                    .when().post(getAddToCartEndpoint());
            response.then().assertThat().statusCode(HTTP_OK);
            logger.info("The product was added to cart successfully");
        } catch (IOException e) {
            logger.error("Add to cart action has error" + e.getMessage());
            e.getStackTrace();
        }

    }

    private void addProductToCartWithModelPayload() {
        String expectedCodeValue = "2876350";
        Integer expectedQuantityValue = 1;
//        Product product = ModelBuilder.of(Product::new).with(Product::setCode, "2876350").build();
//        Cart cartPayload = ModelBuilder.of(Cart::new).with(Cart::setProduct, product).with(Cart::setQuantity, 1).build();
        Product product = Product.builder().code("2876350").build();
        Cart cartPayload = Cart.builder().quantity(1).product(product).build();
//        Cart cartPayload = new Cart();
//        cartPayload.setQuantity(1);
//        Product productPayload = new Product();
//        productPayload.setCode("2876350");
//        cartPayload.setProduct(productPayload);
        Response response = given().spec(requestSpecification()).body(cartPayload)
                .when().post(getAddToCartEndpoint());
        response.then().assertThat().statusCode(200);
        logger.info("The product was added to cart successfully");
        Assert.assertEquals(expectedQuantityValue, response.jsonPath().get("entry.quantity"));
        Assert.assertEquals(expectedCodeValue, response.jsonPath().get("entry.product.code"));
    }

    private String getAddToCartEndpoint() {
        return "/api/v2/kvn/users/anonymous/carts/" + whatIsThe("guid") + "/entries";
    }

}

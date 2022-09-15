package base;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import utilities.Configuration;

@Listeners(ExtentReporterNG.class)
public class BaseTest {


    protected BaseTest(String basePath) {
        this.basePath = basePath;
    }

    protected RequestSpecification request;
    protected String basePath;
    protected Response response;


    @BeforeMethod
    public void setup() {
      request =  RestAssured.given()
                .baseUri(Configuration.get("base_uri"))
                .basePath(basePath)
                .log()
              .uri();
    }

}

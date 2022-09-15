package apitests;

import base.BaseTest;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import utilities.Configuration;


import static matchers.CustomHamcrestMatcher.matchAnyContaining;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.matchers.JUnitMatchers.everyItem;


public class SearchITunesTests extends BaseTest {

    private final String term = "Yelp";
    String compositeTerm = "Jack+Johnson";

    public SearchITunesTests() {
        super(Configuration.get("search_base_path"));
    }


    public void searchTermWithDefaults() {
        request
                .queryParam("term", term)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount", equalTo(50))
                .body("results.country", everyItem(equalTo("USA")))
                .body("results.currency", everyItem(equalTo("USD")));

    }

    @Test
    public void searchWithoutTerm() {
        request
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount",equalTo(0));
    }

    @Test
    public void searchByTermWithLimit() {

        request
                .queryParam("term", term)
                .queryParam("limit", 25)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount", equalTo(25));
    }


    @Test
    public void limitMoreThan200(){
        request
                .queryParam("term","star")
                .queryParam("limit",201)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount",equalTo(200));
    }

    @Test
    public void searchByCountry() {
        request
                .queryParam("term", compositeTerm)
                .queryParam("country", "ca")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.country", everyItem(equalTo("CAN")))
                .body("results.currency", everyItem(equalTo("CAD")));
    }


    @Test
    public void searchByMediaType() {
        request
                .queryParam("term", "Star Wars")
                .queryParam("media", "audiobook")
                .queryParam("limit", 10)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.wrapperType", everyItem(equalTo("audiobook")));
    }


    @Test
    public void searchByMediaTypeAndEntity() {
        request
                .queryParam("term", "Jennifer")
                .queryParam("media", "movie")
                .queryParam("entity", "movieArtist")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.wrapperType", everyItem(equalTo("artist")))
                .body("results.artistName", everyItem(containsString("Jennifer")));
    }

    @Test
    public void searchByArtistName() {
        String[] terms = compositeTerm.split("\\+");
        request
                .queryParam("term", compositeTerm)
                .queryParam("entity", "allArtist")
                .queryParam("attribute", "allArtistTerm")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.artistName",
                        everyItem(anyOf(matchAnyContaining(terms))));
    }

    @Test(dataProvider = "testDataProvider")
    public void searchByInvalidProperty(String paramname, String key) {
        request
                .queryParam("term", "Jack+Johnson")
                .queryParam(paramname, "abc")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(equalTo(400))
                .body("errorMessage", equalTo("Invalid value(s) for key(s): ["+key+"]"));
    }

    @DataProvider
    public Object[][] testDataProvider() {
        return new Object[][]{
                {"country","country"},
                {"media", "mediaType"},
                {"entity", "resultEntity"},
                {"attribute", "attributeType"},
                {"lang","language"}
        };
    }



}

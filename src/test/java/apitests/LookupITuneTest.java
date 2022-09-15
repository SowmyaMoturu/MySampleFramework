package apitests;

import base.BaseTest;
import org.testng.annotations.Test;
import utilities.Configuration;

import static matchers.CustomHamcrestMatcher.matchAny;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.core.AnyOf.anyOf;
import static org.junit.matchers.JUnitMatchers.everyItem;

public class LookupITuneTest extends BaseTest {

    protected LookupITuneTest(String basePath) {
        super(Configuration.get("lookup_base_path"));
    }

    @Test
    public void lookupByArtist() {
       request
               .queryParam("id", 909253)
       .when()
               .get()
       .then()
               .assertThat()
               .statusCode(200)
               .body("results.artistName",everyItem(equalTo("Jack Johnson")));
    }

    @Test
    public void lookupByISBN() {
        request
                .queryParam("isbn", "9780316069359")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.trackName",everyItem(equalTo("The Fifth Witness")));
    }

    @Test
    public void lookupEntityByArtistWithOutLimit() {
            request
                    .queryParam("amgArtistId",  468749)
                    .queryParam("entity","album")
            .when()
                     .get()
            .then()
                    .assertThat()
                    .statusCode(200)
                    .body("results.artistName",everyItem(equalTo("Jack Johnson")));
    }


    @Test
    public void lookupEntityByArtistWithLimit() {
        request
                .queryParam("amgArtistId",  468749)
                .queryParam("limit",5)
                .queryParam("entity","album")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount", equalTo(6))
                .body("results.artistName",everyItem(equalTo("Jack Johnson")));
    }


    @Test
    public void lookupEntitiesByMultipleArtists() {

        String[] artists = {"Jack Johnson", "U2"};
        request
                .queryParam("amgArtistId", 468749, 5723)
                .queryParam("entity","album")
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("results.artistName",everyItem(matchAny(artists)));
    }

    @Test
    public void lookupEntitiesByMultipleArtistsWithLimit() {

        request
                .queryParam("amgArtistId", 468749, 5723)
                .queryParam("entity","album")
                .queryParam("limit", 5)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200)
                .body("resultCount", equalTo(12))
                .body("results.artistName",anyOf(equalTo("Jack Johnson"),
                        equalTo("U2")));
    }

    @Test
    public void lookupByAlbumIds() {
        String idParam = "15175,15176,15177,15178,15183,15184,15187,1519,15191,15195,15197,15198";
        request
                .queryParam("amgAlbumId", idParam)
        .when()
                .get()
        .then()
                .assertThat()
                .statusCode(200);
    }
}

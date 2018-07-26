package pl.sszepiet.acceptance;

import io.restassured.http.ContentType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import pl.sszepiet.customer.Customer;

import java.util.UUID;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class CustomerControllerAcceptanceTest extends BaseAcceptanceTest {

    @Override
    String getPath() {
        return "/customers";
    }

    @Test
    public void shouldCreateNewCustomer() {
        String locationHeader =
        given().
                log().all().
                body(readResourceAsString("requests/new_customer.json")).
                contentType(ContentType.JSON).
        when().
                post(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().
                header(HttpHeaders.LOCATION);
        assertNotNull(locationHeader);
        Customer customer = entityManager.find(Customer.class, UUID.fromString(locationHeader.replaceAll(".*/(.+)", "$1")));
        assertEquals(customer.getFirstName(), "Szymon");
        assertEquals(customer.getLastName(), "Szepietowski");
    }

    @Test
    public void shouldGetExistingCustomer() {
        Customer customer = getExistingCustomer();
        get(getUrl() + "/" + customer.getId()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("customerId", equalTo(customer.getId().toString())).
                body("firstName", equalTo(customer.getFirstName())).
                body("lastName", equalTo(customer.getLastName()));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidBodyIsProvided() {
        given().
                log().all().
                body(readResourceAsString("requests/invalid_customer.json")).
                contentType(ContentType.JSON).
        when().
                post(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }
}

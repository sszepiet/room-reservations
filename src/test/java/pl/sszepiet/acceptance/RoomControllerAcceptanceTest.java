package pl.sszepiet.acceptance;

import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import pl.sszepiet.reservation.ReservationPeriod;

import java.math.BigDecimal;
import java.time.LocalDate;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

public class RoomControllerAcceptanceTest extends BaseAcceptanceTest {
    @Override
    String getPath() {
        return "/rooms";
    }

    @Test
    public void shouldReturnAllRoomsWithNoQueryParamsSpecified() {
        get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded.roomResources", hasSize(getRooms().size()));
    }

    @Test
    public void shouldReturnWarsawRoomsOnlyWhenQueriedForTheCity() {
        String city = "Warsaw";
        given().
                queryParam("city", city).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded.roomResources", hasSize((int) getRooms().stream()
                        .filter(room -> room.getCity().equals(city))
                        .count()));

    }

    @Test
    public void shouldReturnWarsawRoomsMatchingPriceRange() {
        String city = "Warsaw";
        BigDecimal minimalDailyPrice = BigDecimal.valueOf(90.0);
        BigDecimal maximalDailyPrice = BigDecimal.valueOf(110.50);
        given().
                queryParam("city", city).
                queryParam("minimalDailyPrice", minimalDailyPrice).
                queryParam("maximalDailyPrice", maximalDailyPrice).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded.roomResources", hasSize((int) getRooms().stream()
                        .filter(room -> room.getCity().equals(city) &&
                                room.getDailyPrice().compareTo(minimalDailyPrice) >= 0 &&
                                room.getDailyPrice().compareTo(maximalDailyPrice) <= 0)
                        .count()));
    }

    @Test
    public void shouldReturnWarsawRoomsThatAreNotBooked() {
        String city = "Warsaw";
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = LocalDate.now().plusDays(7);
        given().
                queryParam("city", city).
                queryParam("checkIn", checkIn.toString()).
                queryParam("checkOut", checkOut.toString()).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded.roomResources", hasSize((int) getRooms().stream()
                        .filter(room -> room.getCity().equals(city) && !room.isNotReservableWithin(new ReservationPeriod(checkIn, checkOut)))
                        .count()));
    }

    @Test
    public void shouldReturnBadRequestWhenInvalidQueryParametersArePassed() {
        LocalDate checkIn = LocalDate.now();
        given().
                queryParam("checkIn", checkIn.toString()).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.BAD_REQUEST.value());
    }
}

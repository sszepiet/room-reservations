package pl.sszepiet.acceptance;

import io.restassured.http.ContentType;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.testng.annotations.Test;
import pl.sszepiet.customer.Customer;
import pl.sszepiet.reservation.Reservation;
import pl.sszepiet.reservation.ReservationPeriod;
import pl.sszepiet.reservation.ReservationStatus;
import pl.sszepiet.room.Room;

import java.time.LocalDate;
import java.time.Month;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.nullValue;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotNull;

public class ReservationControllerAcceptanceTest extends BaseAcceptanceTest {
    @Override
    String getPath() {
        return "/reservations";
    }

    @Test
    public void shouldReturnReservationsOfCustomer() {
        Customer customerWithReservations = getCustomers().stream()
                .filter(customer -> !customer.getReservations().isEmpty())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        int pageSize = 5;
        given().
                log().all().
                queryParam("page", 0).
                queryParam("size", pageSize).
                header("Customer-Id", customerWithReservations.getId().toString()).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded.reservationResources", hasSize(Math.min(pageSize, customerWithReservations.getReservations().size())));
    }

    @Test
    public void shouldNotReturnReservationsForNonExistingCustomer() {
        given().
                log().all().
                header("Customer-Id", UUID.randomUUID().toString()).
        when().
                get(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.OK.value()).
                body("_embedded", nullValue());
    }

    @Test
    public void shouldCancelExistingReservation() {
        Customer customerWithReservations = getCustomers().stream()
                .filter(customer -> !customer.getReservations().isEmpty())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        UUID customerWithReservationsId = customerWithReservations.getId();
        Reservation reservation = customerWithReservations.getReservations().iterator().next();
        entityManager.detach(customerWithReservations);
        entityManager.detach(reservation);
        given().
                log().all().
                header("Customer-Id", customerWithReservationsId.toString()).
        when().
                put(getUrl() + "/" + reservation.getId().toString()).
        then().
                log().all().
                statusCode(HttpStatus.NO_CONTENT.value());
        reservation = entityManager.find(Reservation.class, reservation.getId());
        assertEquals(reservation.getReservationStatus(), ReservationStatus.CANCELLED);
    }

    @Test
    public void shouldReturnNotFoundWhenCancellingReservationOnBehalfOfNonExistingCustomer() {
        Customer customerWithReservations = getCustomers().stream()
                .filter(customer -> !customer.getReservations().isEmpty())
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        Reservation reservation = customerWithReservations.getReservations().iterator().next();
        given().
                log().all().
                header("Customer-Id", UUID.randomUUID().toString()).
        when().
                put(getUrl() + "/" + reservation.getId().toString()).
        then().
                log().all().
                statusCode(HttpStatus.NOT_FOUND.value());
    }

    @Test
    public void shouldCreateNewReservationForFreeRoom() throws JSONException {
        LocalDate checkIn = LocalDate.of(2222, Month.JANUARY, 10);
        LocalDate checkOut = checkIn.plusDays(1);
        Room reservableRoom = getRooms().stream()
                .filter(room -> !room.isNotReservableWithin(new ReservationPeriod(checkIn, checkOut)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        Customer existingCustomer = getExistingCustomer();
        JSONObject reservationPeriod = new JSONObject()
                .put("checkIn", checkIn.toString())
                .put("checkOut", checkOut.toString());
        String reservationRequest = new JSONObject()
                .put("roomId", reservableRoom.getId().toString())
                .put("reservationPeriod", reservationPeriod)
                .toString();

        String locationHeader =
        given().
                log().all().
                header("Customer-Id", existingCustomer.getId().toString()).
                contentType(ContentType.JSON).
                body(reservationRequest).
        when().
                post(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.CREATED.value()).
                extract().
                header(HttpHeaders.LOCATION);

        assertNotNull(locationHeader);
        Reservation reservation = entityManager.find(Reservation.class, UUID.fromString(locationHeader.replaceAll(".*/(.+)", "$1")));
        assertEquals(reservation.getCheckIn(), checkIn);
        assertEquals(reservation.getCheckOut(), checkOut);
        assertEquals(reservation.getCreationDate().toLocalDate(), LocalDate.now());
        assertEquals(reservation.getCustomer(), existingCustomer);
        assertEquals(reservation.getReservationStatus(), ReservationStatus.OPEN);
        assertEquals(reservation.getRoom(), reservableRoom);
    }

    @Test
    public void shouldReturnPreconditionFailedWhenAttemptingToReserveUnavailableRoom() throws JSONException {
        LocalDate checkIn = LocalDate.now();
        LocalDate checkOut = checkIn.plusDays(1);
        Room unavailableRoom = getRooms().stream()
                .filter(room -> room.isNotReservableWithin(new ReservationPeriod(checkIn, checkOut)))
                .findFirst()
                .orElseThrow(IllegalStateException::new);
        Customer existingCustomer = getExistingCustomer();

        JSONObject reservationPeriod = new JSONObject()
                .put("checkIn", checkIn.toString())
                .put("checkOut", checkOut.toString());
        String reservationRequest = new JSONObject()
                .put("roomId", unavailableRoom.getId().toString())
                .put("reservationPeriod", reservationPeriod)
                .toString();

        given().
                log().all().
                header("Customer-Id", existingCustomer.getId().toString()).
                contentType(ContentType.JSON).
                body(reservationRequest).
        when().
                post(getUrl()).
        then().
                log().all().
                statusCode(HttpStatus.PRECONDITION_FAILED.value());
    }
}

package pl.sszepiet.room.constraint;

import org.testng.annotations.Test;
import pl.sszepiet.room.RoomSearchParameters;

import java.time.LocalDate;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

@Test
public class ReservationDatesValidatorTest {

    private final ReservationDatesValidator testee = new ReservationDatesValidator();

    public void shouldReturnTrueIfNoDatesAreSet() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(null, null);
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertTrue(valid);
    }

    public void shouldReturnTrueWhenCheckInIsEqualToday() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now(), LocalDate.now().plusDays(1));
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertTrue(valid);
    }

    public void shouldReturnTrueWhenCheckInIsAfterToday() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2));
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertTrue(valid);
    }

    public void shouldReturnFalseWhenCheckInIsNotSet() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(null, LocalDate.now().plusDays(2));
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertFalse(valid);
    }

    public void shouldReturnFalseWhenCheckOutIsNotSet() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now(), null);
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertFalse(valid);
    }

    public void shouldReturnFalseWhenCheckInIsBeforeToday() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now().minusDays(1), LocalDate.now().plusDays(1));
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertFalse(valid);
    }

    public void shouldReturnFalseWhenCheckOutIsBeforeCheckIn() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now(), LocalDate.now().minusDays(1));
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertFalse(valid);
    }

    public void shouldReturnFalseWhenCheckOutIsEqualCheckIn() {
        // given
        RoomSearchParameters roomSearchParameters = createWithDates(LocalDate.now(), LocalDate.now());
        // when
        boolean valid = testee.isValid(roomSearchParameters, null);
        // then
        assertFalse(valid);
    }

    private RoomSearchParameters createWithDates(LocalDate checkIn, LocalDate checkOut) {
        return new RoomSearchParameters(null, checkIn, checkOut, null, null);
    }
}
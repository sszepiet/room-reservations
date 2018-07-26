package pl.sszepiet.reservation;

import org.testng.annotations.Test;

import java.time.LocalDate;

import static org.testng.Assert.*;

public class ReservationTest {

    @Test
    public void shouldNotCollideWhenReservationPeriodIsBeforeCurrentReservation() {
        // given
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.now().minusDays(1), LocalDate.now());
        Reservation reservation = Reservation.create(null, null, new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(1)));
        // when
        boolean isColliding = reservation.isColliding(reservationPeriod);
        // then
        assertFalse(isColliding);
    }

    @Test
    public void shouldNotCollideWhenReservationPeriodIsAfterCurrentReservation() {
        // given
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(1));
        Reservation reservation = Reservation.create(null, null, new ReservationPeriod(LocalDate.now().minusDays(1), LocalDate.now()));
        // when
        boolean isColliding = reservation.isColliding(reservationPeriod);
        // then
        assertFalse(isColliding);
    }

    @Test
    public void shouldCollideWhenCheckInIsBetweenCurrentReservation() {
        // given
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(2));
        Reservation reservation = Reservation.create(null, null, new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(1)));
        // when
        boolean isColliding = reservation.isColliding(reservationPeriod);
        // then
        assertTrue(isColliding);
    }

    @Test
    public void shouldCollideWhenCheckOutIsBetweenCurrentReservation() {
        // given
        ReservationPeriod reservationPeriod = new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(2));
        Reservation reservation = Reservation.create(null, null, new ReservationPeriod(LocalDate.now().plusDays(1), LocalDate.now().plusDays(2)));
        // when
        boolean isColliding = reservation.isColliding(reservationPeriod);
        // then
        assertTrue(isColliding);
    }
}
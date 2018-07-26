package pl.sszepiet.room;

import lombok.Value;
import pl.sszepiet.room.constraint.ReservationDatesValid;

import java.math.BigDecimal;
import java.time.LocalDate;

@Value
@ReservationDatesValid
public class RoomSearchParameters {
    private String city;
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal minimalDailyPrice;
    private BigDecimal maximalDailyPrice;

    public boolean hasNoneDatesSet() {
        return (checkIn == null && checkOut == null);
    }

    public boolean hasBothDatesSet() {
        return checkIn != null && checkOut != null;
    }
}

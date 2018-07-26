package pl.sszepiet.reservation;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
public class ReservationPeriod {

    @NotNull
    private LocalDate checkIn;

    @NotNull
    private LocalDate checkOut;
}

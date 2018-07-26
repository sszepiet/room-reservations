package pl.sszepiet.reservation;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
class ReservationRequest {
    @NotNull
    private UUID roomId;
    @NotNull
    private ReservationPeriod reservationPeriod;
}

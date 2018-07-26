package pl.sszepiet.reservation;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;
import pl.sszepiet.room.RoomResource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
@Builder
class ReservationResource extends ResourceSupport {

    private UUID reservationId;

    private RoomResource room;

    private LocalDateTime creationDate;

    private ReservationStatus reservationStatus;

    private LocalDate checkIn;

    private LocalDate checkOut;
}

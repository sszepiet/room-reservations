package pl.sszepiet.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.LinkBuilderFactory;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ControllerLinkBuilderFactory;
import org.springframework.stereotype.Component;
import pl.sszepiet.room.RoomResourceAssembler;

@Component
class ReservationResourceAssembler implements ResourceAssembler<Reservation, ReservationResource> {

    private final RoomResourceAssembler roomResourceAssembler;

    @Autowired
    public ReservationResourceAssembler(RoomResourceAssembler roomResourceAssembler) {
        this.roomResourceAssembler = roomResourceAssembler;
    }

    @Override
    public ReservationResource toResource(Reservation entity) {
        ReservationResource reservationResource = ReservationResource.builder()
                .reservationId(entity.getId())
                .checkIn(entity.getCheckIn())
                .checkOut(entity.getCheckOut())
                .creationDate(entity.getCreationDate())
                .reservationStatus(entity.getReservationStatus())
                .room(roomResourceAssembler.toResource(entity.getRoom()))
                .build();
        if (reservationResource.getReservationStatus() == ReservationStatus.OPEN) {
            reservationResource.add(ControllerLinkBuilder.linkTo(ReservationController.class)
                    .slash(reservationResource.getReservationId())
                    .withRel("cancel"));
        }
        return reservationResource;
    }
}

package pl.sszepiet.room;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.stereotype.Component;
import pl.sszepiet.reservation.ReservationController;

@Component
public class RoomResourceAssembler implements ResourceAssembler<Room, RoomResource> {

    @Override
    public RoomResource toResource(Room entity) {
        RoomResource roomResource = new RoomResource(entity.getId(), entity.getCity(), entity.getDailyPrice());
        roomResource.add(ControllerLinkBuilder.linkTo(ReservationController.class).withRel("reserve"));
        return roomResource;
    }

}

package pl.sszepiet.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resources;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "rooms")
class RoomController {

    private final RoomService roomService;
    private final RoomResourceAssembler roomResourceAssembler;

    @Autowired
    public RoomController(RoomService roomService, RoomResourceAssembler roomResourceAssembler) {
        this.roomService = roomService;
        this.roomResourceAssembler = roomResourceAssembler;
    }

    @GetMapping
    public Resources<RoomResource> findRooms(@Valid RoomSearchParameters roomSearchParameters) {
        Collection<Room> rooms = roomService.findRoomsMatching(roomSearchParameters);
        List<RoomResource> roomResources = rooms.stream()
                .map(roomResourceAssembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(roomResources);
    }
}

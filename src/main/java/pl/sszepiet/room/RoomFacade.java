package pl.sszepiet.room;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sszepiet.exception.NotFoundException;

import java.util.UUID;

@Component
public class RoomFacade {

    private final RoomRepository roomRepository;

    @Autowired
    public RoomFacade(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    public Room findRoomByIdLocking(UUID roomId) {
        return roomRepository.findByIdLockingAndFetchingReservations(roomId).orElseThrow(NotFoundException::new);
    }
}

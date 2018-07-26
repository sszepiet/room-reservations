package pl.sszepiet.room;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
class RoomService {

    private final RoomRepository roomRepository;

    public RoomService(RoomRepository roomRepository) {
        this.roomRepository = roomRepository;
    }

    Collection<Room> findRoomsMatching(RoomSearchParameters roomSearchParameters) {
        Specification<Room> roomSpecification = new RoomSearchParametersToSpecification().apply(roomSearchParameters);
        return roomRepository.findAll(roomSpecification);
    }
}

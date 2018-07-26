package pl.sszepiet.room;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;
import java.util.UUID;

interface RoomRepository extends JpaRepository<Room, UUID>, JpaSpecificationExecutor<Room> {

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select r from Room r left join r.reservations res where r.id = :id")
    Optional<Room> findByIdLockingAndFetchingReservations(@Param(value = "id") UUID id);
}

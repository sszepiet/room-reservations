package pl.sszepiet.reservation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    Page<Reservation> findAllByCustomerId(UUID customerId, Pageable pageable);

    Optional<Reservation> findByIdAndCustomerId(UUID id, UUID customerId);
}

package pl.sszepiet.reservation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pl.sszepiet.customer.Customer;
import pl.sszepiet.customer.CustomerFacade;
import pl.sszepiet.exception.NotFoundException;
import pl.sszepiet.exception.PreconditionFailedException;
import pl.sszepiet.room.Room;
import pl.sszepiet.room.RoomFacade;

import java.util.UUID;

@Service
class ReservationService {

    private static final Sort DEFAULT_SORT = Sort.by(Sort.Direction.DESC, "creationDate");

    private final CustomerFacade customerFacade;
    private final RoomFacade roomFacade;
    private final ReservationRepository reservationRepository;

    public ReservationService(CustomerFacade customerFacade, RoomFacade roomFacade, ReservationRepository reservationRepository) {
        this.customerFacade = customerFacade;
        this.roomFacade = roomFacade;
        this.reservationRepository = reservationRepository;
    }

    @Transactional
    public UUID createReservation(ReservationRequest reservationRequest, UUID customerId) {
        Customer customer = customerFacade.findById(customerId);
        Room room = roomFacade.findRoomByIdLocking(reservationRequest.getRoomId());
        if (room.isNotReservableWithin(reservationRequest.getReservationPeriod())) {
            throw new PreconditionFailedException();
        }
        Reservation reservation = reservationRepository.save(Reservation.create(customer, room, reservationRequest.getReservationPeriod()));
        return reservation.getId();
    }

    Page<Reservation> findAllByCustomerId(UUID customerId, Pageable pageable) {
        PageRequest withDefaultSorting = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), DEFAULT_SORT);
        return reservationRepository.findAllByCustomerId(customerId, withDefaultSorting);
    }

    Reservation findByIdAndCustomerId(UUID id, UUID customerId) {
        return reservationRepository.findByIdAndCustomerId(id, customerId).orElseThrow(NotFoundException::new);
    }

    @Transactional
    public void cancelReservation(UUID id, UUID customerId) {
        Reservation toCancel = findByIdAndCustomerId(id, customerId);
        toCancel.cancel();
        reservationRepository.save(toCancel);
    }
}

package pl.sszepiet.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final ReservationResourceAssembler reservationResourceAssembler;

    @Autowired
    public ReservationController(ReservationService reservationService, ReservationResourceAssembler reservationResourceAssembler) {
        this.reservationService = reservationService;
        this.reservationResourceAssembler = reservationResourceAssembler;
    }

    @PostMapping
    public ResponseEntity<Void> reserveRoom(@Valid @RequestBody ReservationRequest reservationRequest, @RequestHeader("Customer-Id") UUID customerId) {
        UUID reservationId = reservationService.createReservation(reservationRequest, customerId);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(reservationId)
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping
    public PagedResources<ReservationResource> findReservations(@RequestHeader("Customer-Id") UUID customerId, Pageable pageable,
                                                                PagedResourcesAssembler<Reservation> assembler) {
        Page<Reservation> reservations = reservationService.findAllByCustomerId(customerId, pageable);
        return assembler.toResource(reservations, reservationResourceAssembler);
    }

    @GetMapping(path = "/{id}")
    public ReservationResource findReservation(@PathVariable UUID id, @RequestHeader("Customer-Id") UUID customerId) {
        Reservation reservation = reservationService.findByIdAndCustomerId(id, customerId);
        return reservationResourceAssembler.toResource(reservation);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Void> cancelReservation(@PathVariable UUID id, @RequestHeader("Customer-Id") UUID customerId) {
        reservationService.cancelReservation(id, customerId);
        return ResponseEntity.noContent().build();
    }
}

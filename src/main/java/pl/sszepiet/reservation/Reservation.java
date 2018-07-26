package pl.sszepiet.reservation;

import lombok.*;
import pl.sszepiet.customer.Customer;
import pl.sszepiet.room.Room;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@EqualsAndHashCode(of = "id")
@ToString
@Getter
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "customerId", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "roomId", nullable = false)
    private Room room;

    @Column(nullable = false)
    private LocalDateTime creationDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus reservationStatus;

    @Column(nullable = false)
    private LocalDate checkIn;

    @Column(nullable = false)
    private LocalDate checkOut;

    public static Reservation create(Customer customer, Room room, ReservationPeriod reservationPeriod) {
        Reservation reservation = new Reservation();
        reservation.customer = customer;
        reservation.room = room;
        reservation.reservationStatus = ReservationStatus.OPEN;
        reservation.checkIn = reservationPeriod.getCheckIn();
        reservation.checkOut = reservationPeriod.getCheckOut();
        reservation.creationDate = LocalDateTime.now();
        return reservation;
    }

    void cancel() {
        this.reservationStatus = ReservationStatus.CANCELLED;
    }

    public boolean isColliding(ReservationPeriod reservationPeriod) {
        return isCheckInInterfering(reservationPeriod.getCheckIn()) || isCheckOutInterfering(reservationPeriod.getCheckOut());
    }

    private boolean isCheckInInterfering(LocalDate newCheckIn) {
        return newCheckIn.isBefore(checkOut) && (newCheckIn.isAfter(checkIn) || newCheckIn.isEqual(checkIn));
    }

    private boolean isCheckOutInterfering(LocalDate newCheckOut) {
        return newCheckOut.isAfter(checkIn) && (newCheckOut.isBefore(checkOut) || newCheckOut.isEqual(checkOut));
    }
}

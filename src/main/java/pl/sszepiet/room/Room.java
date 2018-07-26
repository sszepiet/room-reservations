package pl.sszepiet.room;

import lombok.*;
import pl.sszepiet.reservation.Reservation;
import pl.sszepiet.reservation.ReservationPeriod;
import pl.sszepiet.reservation.ReservationStatus;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Entity
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@Setter(AccessLevel.PACKAGE)
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = "id")
@ToString(exclude = "reservations")
@Getter
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String city;

    @Column(nullable = false)
    private BigDecimal dailyPrice;

    @OneToMany(mappedBy = "room")
    private Set<Reservation> reservations;

    public boolean isNotReservableWithin(ReservationPeriod reservationPeriod) {
        return reservations.stream()
                .filter(reservation -> reservation.getReservationStatus() != ReservationStatus.CANCELLED && reservation.isColliding(reservationPeriod))
                .findFirst()
                .isPresent();
    }

}

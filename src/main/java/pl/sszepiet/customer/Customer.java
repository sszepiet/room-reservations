package pl.sszepiet.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import pl.sszepiet.reservation.Reservation;

import javax.persistence.*;
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
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String firstName;

    private String lastName;

    @OneToMany(mappedBy = "customer")
    private Set<Reservation> reservations;
}

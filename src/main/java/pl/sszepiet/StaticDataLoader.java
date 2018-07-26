package pl.sszepiet;

import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pl.sszepiet.customer.Customer;
import pl.sszepiet.reservation.Reservation;
import pl.sszepiet.reservation.ReservationPeriod;
import pl.sszepiet.room.Room;

import javax.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Component
public class StaticDataLoader implements CommandLineRunner {

    private static final int NUMBER_OF_RESERVATIONS = 20;

    private static final Logger LOG = LoggerFactory.getLogger(StaticDataLoader.class);

    private final EntityManager entityManager;

    @Autowired
    public StaticDataLoader(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Getter
    private List<Room> rooms;
    @Getter
    private List<Customer> customers;
    @Getter
    private List<Reservation> reservations;

    @Override
    @Transactional
    public void run(String... strings) throws Exception {
        rooms = Stream.of(Room.builder().city("Warsaw").dailyPrice(BigDecimal.valueOf(100.0)).build(),
                Room.builder().city("Warsaw").dailyPrice(BigDecimal.valueOf(150.75)).build(),
                Room.builder().city("Warsaw").dailyPrice(BigDecimal.valueOf(57.50)).build(),
                Room.builder().city("Cracov").dailyPrice(BigDecimal.valueOf(75)).build(),
                Room.builder().city("Wrocław").dailyPrice(BigDecimal.valueOf(98)).build())
                .map(entityManager::merge)
                .collect(Collectors.toList());
        customers = Stream.of(Customer.builder().firstName("Jan").lastName("Kowalski").build(),
                Customer.builder().firstName("Aleksander").lastName("Nowak").build())
                .map(entityManager::merge)
                .collect(Collectors.toList());
        reservations = createReservationPeriods()
                .map(reservationPeriod -> entityManager.merge(Reservation.create(customers.get(0), rooms.get(0), reservationPeriod)))
                .collect(Collectors.toList());
        rooms.forEach(room -> LOG.info("Inserted room: {}", room));
        customers.forEach(customer -> LOG.info("Inserted customer: {}", customer));
        reservations.forEach(reservation -> LOG.info("Inserted reservation: {}", reservation));
    }

    private Stream<ReservationPeriod> createReservationPeriods() {
        return IntStream.range(0, NUMBER_OF_RESERVATIONS)
                .mapToObj(value -> new ReservationPeriod(LocalDate.now().plusDays(value * 7), LocalDate.now().plusDays((value + 1) * 7)));
    }
}

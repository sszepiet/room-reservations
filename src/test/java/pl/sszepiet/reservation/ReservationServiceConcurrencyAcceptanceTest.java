package pl.sszepiet.reservation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;
import pl.sszepiet.StaticDataLoader;

import java.time.LocalDate;
import java.util.UUID;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.jayway.awaitility.Awaitility.await;

@SpringBootTest
public class ReservationServiceConcurrencyAcceptanceTest extends AbstractTestNGSpringContextTests {

    private static final int CONCURRENCY = 2;
    private static final ReservationPeriod RESERVATION_PERIOD = new ReservationPeriod(LocalDate.now(), LocalDate.now().plusDays(7L));

    @Autowired
    private StaticDataLoader staticDataLoader;
    @Autowired
    private ReservationService reservationService;
    @Autowired
    private ReservationRepository reservationRepository;

    @Test
    public void shouldLockRoomForReservation() {
        // given
        long initialReservationsCount = reservationRepository.count();
        CyclicBarrier cyclicBarrier = new CyclicBarrier(CONCURRENCY);
        UUID customerId = staticDataLoader.getCustomers().iterator().next().getId();
        // when
        for (int i = 0; i < CONCURRENCY; i++) {
            new Thread(new Reservator(cyclicBarrier, reservationService, createRequest(), customerId)).start();
        }
        // then
        await()
                .atMost(10L, TimeUnit.SECONDS)
                .until(() -> initialReservationsCount + 1 == reservationRepository.count());
    }

    private ReservationRequest createRequest() {
        return new ReservationRequest(staticDataLoader.getRooms().get(1).getId(), RESERVATION_PERIOD);
    }

    private static class Reservator implements Runnable {

        private final CyclicBarrier cyclicBarrier;
        private final ReservationService reservationService;
        private final ReservationRequest reservationRequest;
        private final UUID customerId;

        private Reservator(CyclicBarrier cyclicBarrier, ReservationService reservationService, ReservationRequest reservationRequest, UUID customerId) {
            this.cyclicBarrier = cyclicBarrier;
            this.reservationService = reservationService;
            this.reservationRequest = reservationRequest;
            this.customerId = customerId;
        }

        @Override
        public void run() {
            try {
                cyclicBarrier.await();
                reservationService.createReservation(reservationRequest, customerId);
            } catch (Throwable e) {
                Logger.getAnonymousLogger().warning("Thread exception: " + e.getMessage());
            }
        }


    }
}
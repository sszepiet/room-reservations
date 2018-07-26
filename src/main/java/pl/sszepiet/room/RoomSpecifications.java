package pl.sszepiet.room;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import pl.sszepiet.reservation.Reservation;
import pl.sszepiet.reservation.ReservationStatus;
import pl.sszepiet.reservation.Reservation_;

import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import java.math.BigDecimal;
import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class RoomSpecifications {

    static Specification<Room> locatedIn(String city) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get(Room_.city), city);
    }

    static Specification<Room> dailyPriceAtLeast(BigDecimal minimalPrice) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.greaterThanOrEqualTo(root.get(Room_.dailyPrice), minimalPrice));
    }

    static Specification<Room> dailyPriceAtMost(BigDecimal maximalPrice) {
        return ((root, query, criteriaBuilder) -> criteriaBuilder.lessThanOrEqualTo(root.get(Room_.dailyPrice), maximalPrice));
    }

    static Specification<Room> isFreeBetween(LocalDate checkIn, LocalDate checkOut) {
        return ((root, query, criteriaBuilder) -> {
            Subquery<Reservation> subQuery = query.subquery(Reservation.class);
            Root<Reservation> subQueryRoot = subQuery.from(Reservation.class);
            subQuery.select(subQueryRoot);
            Predicate correlation = criteriaBuilder.equal(subQueryRoot.get(Reservation_.room), root);
            Predicate nonCancelled = criteriaBuilder.notEqual(subQueryRoot.get(Reservation_.reservationStatus), ReservationStatus.CANCELLED);
            Predicate checkInBetween = criteriaBuilder.between(subQueryRoot.get(Reservation_.checkIn), checkIn, checkOut.minusDays(1L));
            Predicate checkOutAfter = criteriaBuilder.between(subQueryRoot.get(Reservation_.checkOut), checkIn.plusDays(1), checkOut);
            subQuery.where(criteriaBuilder.and(correlation, criteriaBuilder.and(nonCancelled, criteriaBuilder.or(checkInBetween, checkOutAfter))));
            return criteriaBuilder.exists(subQuery).not();
        });
    }
}

package pl.sszepiet.room;

import org.springframework.data.jpa.domain.Specification;

import java.util.function.Function;

class RoomSearchParametersToSpecification implements Function<RoomSearchParameters, Specification<Room>> {

    @Override
    public Specification<Room> apply(RoomSearchParameters roomSearchParameters) {
        Specification<Room> combinedSpecifications = null;
        if (roomSearchParameters.getCity() != null) {
            combinedSpecifications = combineWithAndOperator(null, RoomSpecifications.locatedIn(roomSearchParameters.getCity()));
        }
        if (roomSearchParameters.getMinimalDailyPrice() != null) {
            combinedSpecifications = combineWithAndOperator(combinedSpecifications, RoomSpecifications.dailyPriceAtLeast(roomSearchParameters.getMinimalDailyPrice()));
        }
        if (roomSearchParameters.getMaximalDailyPrice() != null) {
            combinedSpecifications = combineWithAndOperator(combinedSpecifications, RoomSpecifications.dailyPriceAtMost(roomSearchParameters.getMaximalDailyPrice()));
        }
        if (roomSearchParameters.getCheckIn() != null && roomSearchParameters.getCheckOut() != null) {
            combinedSpecifications = combineWithAndOperator(combinedSpecifications,
                    RoomSpecifications.isFreeBetween(roomSearchParameters.getCheckIn(), roomSearchParameters.getCheckOut()));
        }
        return combinedSpecifications;
    }

    private Specification<Room> combineWithAndOperator(Specification<Room> combinedSpecifications, Specification<Room> toCombine) {
        if (combinedSpecifications == null) {
            return Specification.where(toCombine);
        } else {
            return combinedSpecifications.and(toCombine);
        }
    }
}

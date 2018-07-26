package pl.sszepiet.room.constraint;

import pl.sszepiet.room.RoomSearchParameters;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

class ReservationDatesValidator implements ConstraintValidator<ReservationDatesValid, RoomSearchParameters> {

    @Override
    public boolean isValid(RoomSearchParameters roomSearchParameters, ConstraintValidatorContext constraintValidatorContext) {
        return roomSearchParameters.hasNoneDatesSet() || (roomSearchParameters.hasBothDatesSet()
                && (roomSearchParameters.getCheckIn().isAfter(LocalDate.now()) || roomSearchParameters.getCheckIn().isEqual(LocalDate.now()))
                && (roomSearchParameters.getCheckIn().isBefore(roomSearchParameters.getCheckOut())));
    }

}

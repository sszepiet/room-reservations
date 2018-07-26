package pl.sszepiet.room.constraint;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ReservationDatesValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ReservationDatesValid {
    String message() default "Both dates need to be set if any of them is present and cannot be in the past, checkOut cannot be before checkIn";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

package pl.sszepiet.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
@Getter
public class PreconditionFailedException extends RuntimeException {
    private final String message = "The request could not be fulfilled as resource is in the state that does not allow it";
}

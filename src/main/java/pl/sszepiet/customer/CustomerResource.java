package pl.sszepiet.customer;

import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.hateoas.ResourceSupport;

import java.util.UUID;

@Value
@EqualsAndHashCode(callSuper = false)
class CustomerResource extends ResourceSupport {

    private UUID customerId;

    private String firstName;

    private String lastName;
}

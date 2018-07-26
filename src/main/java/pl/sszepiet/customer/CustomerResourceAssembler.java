package pl.sszepiet.customer;

import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class CustomerResourceAssembler implements ResourceAssembler<Customer, CustomerResource> {

    @Override
    public CustomerResource toResource(Customer entity) {
        return new CustomerResource(entity.getId(), entity.getFirstName(), entity.getLastName());
    }

}

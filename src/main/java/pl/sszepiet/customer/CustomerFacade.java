package pl.sszepiet.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.sszepiet.exception.NotFoundException;

import java.util.UUID;

@Component
public class CustomerFacade {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerFacade(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer findById(UUID uuid) {
        return customerRepository.findById(uuid).orElseThrow(NotFoundException::new);
    }
}

package pl.sszepiet.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import pl.sszepiet.exception.NotFoundException;

import javax.validation.Valid;
import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping(path = "customers")
class CustomerController {

    private final CustomerRepository customerRepository;
    private final CustomerResourceAssembler customerResourceAssembler;

    @Autowired
    public CustomerController(CustomerRepository customerRepository, CustomerResourceAssembler customerResourceAssembler) {
        this.customerRepository = customerRepository;
        this.customerResourceAssembler = customerResourceAssembler;
    }

    @PostMapping
    public ResponseEntity<Void> createCustomer(@RequestBody @Valid CustomerModificationRequest customerModificationRequest) {
        Customer customer = Customer.builder()
                .firstName(customerModificationRequest.getFirstName())
                .lastName(customerModificationRequest.getLastName())
                .build();
        customerRepository.save(customer);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(customer.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping(path = "/{id}")
    public CustomerResource getCustomer(@PathVariable UUID id) {
        return customerRepository.findById(id).map(customerResourceAssembler::toResource)
                .orElseThrow(NotFoundException::new);
    }
}

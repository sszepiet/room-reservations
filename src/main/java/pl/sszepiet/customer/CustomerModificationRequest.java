package pl.sszepiet.customer;

import lombok.Value;

import javax.validation.constraints.NotBlank;

@Value
class CustomerModificationRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

}

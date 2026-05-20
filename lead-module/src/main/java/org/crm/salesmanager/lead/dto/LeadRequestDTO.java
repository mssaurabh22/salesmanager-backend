package org.crm.salesmanager.lead.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

@Data
public class LeadRequestDTO {

    @NotBlank(message = "Customer name is required")
    private String customerName;

    private String businessName;

    @NotBlank(message = "Contact number is required")
    private String contactNumber;

    @Email(message = "Email should be valid")
    private String email;

    @PositiveOrZero(message = "Expected value must be greater than or equal to 0")
    private Double expectedValue;
}

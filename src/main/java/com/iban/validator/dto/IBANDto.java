package com.iban.validator.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

/**
 * This is a Data Transfer Object class to hold IBAN data and pass it to service class
 * @author Soumen Banerjee
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class IBANDto {

    @Pattern(regexp = "^([A-Z]{2}[ \\-]?[0-9]{2})(?=(?:[ \\-]?[A-Z0-9]){9,30}$)((?:[ \\-]?[A-Z0-9]{3,5}){2,7})([ \\-]?[A-Z0-9]{1,3})?$",
             message = "Invalid IBAN, it should look like, e.g: EEXX XXXX XXXX XXXX, EEXX-XXXX-XXXX-XXXX, EEXXXXXXXXXXXXXX")
    @NotNull(message = "IBAN value can not be null")
    @NotBlank(message = "IBAN value can not be blank")
    private String iban;
    private String status;
}

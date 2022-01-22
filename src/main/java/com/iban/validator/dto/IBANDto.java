package com.iban.validator.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Pattern;

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
    private String iban;
    private String status;
}

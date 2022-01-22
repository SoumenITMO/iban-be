package com.iban.validator.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * This is a Data Object Class to format API response with a message
 * @author Soumen Banerjee
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class APIResponse {

    private String apiResponse;
}

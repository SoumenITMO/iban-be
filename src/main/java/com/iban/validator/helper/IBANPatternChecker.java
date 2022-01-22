package com.iban.validator.helper;

import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 * This is IBAN pattern checker service class to validate if input value is following IBAN pattern or not
 * @author Soumen Banerjee
 *
 */
@Service("ibanpattern")
public class IBANPatternChecker {

    /**
     *
     * @param ibanValue iban value either from API Endpoint / uploaded file content
     * @return if value pass through the regex then it will be true or false
     */
    public boolean checkIBANValue(String ibanValue) {
        Pattern IBANRegex = Pattern.compile("^([A-Z]{2}[ \\-]?[0-9]{2})(?=(?:[ \\-]?[A-Z0-9]){9,30}$)((?:[ \\-]?[A-Z0-9]{3,5}){2,7})([ \\-]?[A-Z0-9]{1,3})?$");
        return IBANRegex.matcher(ibanValue).matches();
    }
}

package com.iban.validator.helper;

import java.util.Locale;
import java.math.BigInteger;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * This class is responsible to handle validation of IBAN
 * @author Soumen Banerjee
 */
@AllArgsConstructor
@Service("ibanvalidator")
public class IBANvalidator {

    private static final int PREFIX_LENGTH = 4;
    private static final BigInteger BIG_INTEGER_97 = new BigInteger("97");

    /**
     *
     * @param ibanValue
     * @return return boolean only if the IBAN is checksum is valid
     */
    public boolean validateIBAN(String ibanValue) {

        String strippedIban = ibanValue.replaceAll("[ \\-]", "").toUpperCase(Locale.ROOT);

        String rearrangedIBAN = strippedIban.substring(PREFIX_LENGTH, PREFIX_LENGTH + PREFIX_LENGTH)
                + strippedIban.substring(PREFIX_LENGTH + PREFIX_LENGTH)
                + strippedIban.substring(0, PREFIX_LENGTH);

        BigInteger convertedToInteger = toInteger(replaceCharactersWithDigits(rearrangedIBAN));

        return convertedToInteger.mod(BIG_INTEGER_97).intValue() == 1;
    }

    /**
     *
     * @param iban number formatted string IBAN
     * @return Bigint IBAN
     */
    private static BigInteger toInteger(String iban) {
        return new BigInteger(iban);
    }

    /**
     *
     * @param rearrangedIban Rearranged IBAN value
     * @return number formatted IBAN
     */
    private static String replaceCharactersWithDigits(String rearrangedIban) {

        int letterOffsetValue = -55;
        StringBuilder stringBuffer = new StringBuilder();

        for (char getChar : rearrangedIban.toCharArray()) {
            if (Character.isDigit(getChar)) {
                stringBuffer.append(getChar);
            } else {
                int intValue = (int) getChar + letterOffsetValue;
                stringBuffer.append(intValue);
            }
        }
        return stringBuffer.toString();
    }
}

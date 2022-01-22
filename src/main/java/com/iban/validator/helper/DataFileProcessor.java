package com.iban.validator.helper;

import java.util.List;
import java.util.Arrays;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import java.util.stream.Collectors;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.iban.validator.entity.IBANEntity;
import org.springframework.stereotype.Service;
import com.opencsv.enums.CSVReaderNullFieldIndicator;

/**
 * This is a Data processor class and it is responsible to process uploaded data file
 * @author Soumen Banerjee
 */
@AllArgsConstructor
@Service("datafileprocessor")
public class DataFileProcessor {

    private final IBANvalidator ibaNvalidator;
    private final IBANPatternChecker ibanPatternChecker;

    /**
     * Reads platforms from csv file and returns the as list.
     */
    public List<IBANEntity> processIBANDataFile(String fileName) throws IOException, CsvException {
        List<IBANEntity> generateIbanListEntity = new ArrayList<>();

        CSVReader reader = new CSVReaderBuilder(new FileReader(fileName))
                .withFieldAsNull(CSVReaderNullFieldIndicator.EMPTY_SEPARATORS)
                .withSkipLines(1)
                .build();

        List<String[]> csvData = reader.readAll();

        if(csvData.isEmpty()) {
            reader.close();
            throw new RuntimeException("Invalid File");
        }

        csvData.forEach(getData -> {
            if(getData[0] == null) {
                throw new RuntimeException("Invalid File");
            }
            if(ibanPatternChecker.checkIBANValue(getData[0])) {
                generateIbanListEntity.add(new IBANEntity(0L, transformToIBANFormat(getData[0]),
                        ibaNvalidator.validateIBAN(getData[0]) ? "VALID" : "INVALID"));
            } else {
                throw new RuntimeException("Invalid File");
            }
        });

        return generateIbanListEntity;
    }

    /**
     *
     * @param iban
     * @return formatted IBAN string
     */
    private String transformToIBANFormat(String iban) {
        String[] transformedIBANFormat = iban.replaceAll("[^a-zA-Z0-9]", "").split("(?<=\\G....)");
        return Arrays.stream(transformedIBANFormat).collect(Collectors.joining(" "));
    }
}

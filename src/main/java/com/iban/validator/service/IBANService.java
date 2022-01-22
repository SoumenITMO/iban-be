package com.iban.validator.service;

import java.io.File;
import java.util.List;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileOutputStream;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.JDBCException;
import java.util.stream.Collectors;
import com.iban.validator.dto.IBANDto;
import com.opencsv.exceptions.CsvException;
import com.iban.validator.mapper.IBANMapper;
import com.iban.validator.entity.IBANEntity;
import org.springframework.stereotype.Service;
import com.iban.validator.helper.IBANvalidator;
import com.iban.validator.helper.DataFileProcessor;
import com.iban.validator.repository.IBANRepository;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This is a service class to process IBAN data from REST Controller API
 * @author Soumen Banerjee
 */
@Slf4j
@Service("iban")
@AllArgsConstructor
public class IBANService {

    private final IBANMapper ibanMapper;
    private final IBANvalidator ibaNvalidator;
    private final IBANRepository ibanRepository;
    private final DataFileProcessor dataFileProcessor;

    /**
     * @param appliedFilter return IBAN list based on applied filter
     * @return IBAN list depends on applied filter
     */
    public List<IBANDto> getAllIBAN(String appliedFilter) {
        return filterIBANList(appliedFilter);
    }

    /**
     * @param dataFile provided data file path to insert into DB
     * @throws IOException
     * @throws CsvException
     */
    public void pushDataToDB(MultipartFile dataFile) throws IOException, CsvException, JDBCException {
        File temporaryFile = new File("tmpCsvFile.csv");
        OutputStream outStream = new FileOutputStream(temporaryFile);
        outStream.write(dataFile.getBytes());
        try {
            ibanRepository.saveAll(dataFileProcessor.processIBANDataFile(temporaryFile.getAbsolutePath()));
        } catch (DataIntegrityViolationException constrainViolationException) {
            throw new RuntimeException("Looks like one or more IBAN already in the system, please check the file again.");
        }
        outStream.close();
        new File("tmpCsvFile.csv").delete();
    }

    /**
     * @param ibanDto requested IBAN to validate and store
     * @return list of IBAN
     */
    public void addSingleIBAN(IBANDto ibanDto) {
        try {
            if (ibaNvalidator.validateIBAN(ibanDto.getIban())) {
                ibanRepository.save(new IBANEntity(0L, ibanDto.getIban(), "VALID"));
            } else {
                ibanRepository.save(new IBANEntity(0L, ibanDto.getIban(), "INVALID"));
            }
        } catch (DataIntegrityViolationException constrainException) {
            throw new RuntimeException("IBAN already exists");
        }
    }

    /**
     *
     * @param filterType requested filter
     * @return list of all IBAN
     */
    private List<IBANDto> filterIBANList(String filterType) {
        if (filterType.equals("VALID")) {
            return ibanRepository.findAll().stream()
                    .map(ibanMapper::toDto).filter(getIBANData -> getIBANData.getStatus().equals("VALID"))
                    .collect(Collectors.toList());
        } else if (filterType.equals("INVALID")) {
            return ibanRepository.findAll().stream()
                    .map(ibanMapper::toDto).filter(getIBANData -> getIBANData.getStatus().equals("INVALID"))
                    .collect(Collectors.toList());
        }
        return ibanRepository.findAll().stream().map(ibanMapper::toDto).collect(Collectors.toList());
    }
}

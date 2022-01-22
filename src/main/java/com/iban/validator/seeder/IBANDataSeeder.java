package com.iban.validator.seeder;

import lombok.AllArgsConstructor;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;
import com.iban.validator.helper.DataFileProcessor;
import com.iban.validator.repository.IBANRepository;

/**
 * This is a IBAN data seeder class and it runs after application finished startup
 * @author Soumen Banerjee
 */
@Component
@AllArgsConstructor
public class IBANDataSeeder implements CommandLineRunner {

    private final Environment env;
    private final IBANRepository ibanRepository;
    private final DataFileProcessor dataFileProcessor;

    @Override
    public void run(String... args) throws Exception {
        ibanRepository.saveAll(dataFileProcessor.processIBANDataFile(env.getProperty("seeding.datafile")));
    }
}

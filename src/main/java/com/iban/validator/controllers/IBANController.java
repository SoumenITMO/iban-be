package com.iban.validator.controllers;

import java.util.List;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import com.iban.validator.dto.IBANDto;
import com.iban.validator.dto.APIResponse;
import com.iban.validator.service.IBANService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.validation.annotation.Validated;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This is a REST Controller class to handle IBAN data
 * @author Soumen Banerjee
 */
@Validated
@RestController
@AllArgsConstructor
@RequestMapping("iban")
public class IBANController {

    private final IBANService ibanService;

    /**
     * @param filterType this is an optional field, if no filter type requested then by default it is ALL
     * @return list of all IBAN, when no filter type requested
     */
    @GetMapping(value = "/list")
    public ResponseEntity<List<IBANDto>> getAllibanByAppliedFilter(
            @RequestParam(name = "filter_type", required = false) String filterType) {

        return ResponseEntity.ok().body(ibanService.getAllIBAN(filterType == null ? "" : filterType));
    }

    /**
     * @param file uploaded IBAN data by file
     * @return IBAN file name
     *
     */
    @PostMapping(value = "uploadibanfile")
    public ResponseEntity<APIResponse> uploadFile(@RequestParam("file")MultipartFile file) {
        try {
            ibanService.pushDataToDB(file);
        }
        catch (DataIntegrityViolationException dbException) { return ResponseEntity.badRequest().body(new APIResponse(dbException.getMessage())); }
        catch (Exception exception) { return ResponseEntity.badRequest().body(new APIResponse(exception.getMessage())); }

        return ResponseEntity.ok().body(new APIResponse("uploaded"));
    }

    /**
     * @param iban requested IBAN to check validity and save with a status
     * @return
     */
    @PostMapping(value = "add")
    public ResponseEntity<APIResponse> addIBAN(@Valid @RequestBody IBANDto iban) {
        try {
            ibanService.addSingleIBAN(iban);
        } catch (Exception exception) {
            return ResponseEntity.badRequest().body(new APIResponse(exception.getMessage()));
        }
        return ResponseEntity.ok().body(new APIResponse("IBAN Added"));
    }
}

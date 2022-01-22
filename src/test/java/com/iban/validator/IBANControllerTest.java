package com.iban.validator;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.charset.Charset;
import org.junit.jupiter.api.Test;
import java.util.stream.Collectors;
import com.iban.validator.dto.IBANDto;
import com.iban.validator.dto.APIResponse;
import org.springframework.http.MediaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.beans.factory.annotation.Value;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;

/**
 * This is a test class
 * @author Soumen Banerjee
 */
@SpringBootTest
@AutoConfigureMockMvc
class IBANControllerTest {

    @Value("${seeding.datafile}")
    String mockFile;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void test_for_get_iban_list_rest_api() throws Exception {
        String ibanListAsString = mockMvc.perform(MockMvcRequestBuilders
                        .get("/iban/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(ibanListAsString.contains("BE71 0961 2345 6769"));
    }

    @Test
    void test_for_add_new_iban_and_fetch_all_ibans_if_new_iban_inserted() throws Exception {
        String ibanListAfterNewIbanSaved = mockMvc.perform(MockMvcRequestBuilders
                        .post("/iban/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new IBANDto("SK08 0900 0000 0001 2312 3123", null)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        APIResponse getAPIResponse = mapper.readValue(ibanListAfterNewIbanSaved, APIResponse.class);
        assertEquals("IBAN Added", getAPIResponse.getApiResponse());
    }

    @Test
    void test_for_adding_existing_iban_again_and_throw_error() throws Exception {
        String ibanListAfterNewIbanSaved = mockMvc.perform(MockMvcRequestBuilders
                        .post("/iban/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(new IBANDto("BE71 0961 2345 6769", null)))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn().getResponse().getContentAsString();

        APIResponse getAPIResponse = mapper.readValue(ibanListAfterNewIbanSaved, APIResponse.class);
        assertEquals(getAPIResponse.getApiResponse(), "IBAN already exists");
    }

    @Test
    void test_to_upload_invalid_file_and_throw_error() throws Exception {
        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/iban/uploadibanfile");

        String getData = mockMvc.perform(multipartRequest.file(createMockFileForTest("This is an invalid file data")))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        APIResponse getAPIResponse = mapper.readValue(getData, APIResponse.class);
        assertEquals("Invalid File", getAPIResponse.getApiResponse());
    }

    @Test
    void test_to_upload_valid_file_and_throw_exception_for_already_existed_records() throws Exception {
        String mockFileData = Files.readAllLines(Paths.get(mockFile), Charset.defaultCharset())
                .stream().collect(Collectors.joining("\n"));

        MockMultipartHttpServletRequestBuilder multipartRequest =
                MockMvcRequestBuilders.multipart("/iban/uploadibanfile");

        String getData = mockMvc.perform(multipartRequest.file(createMockFileForTest(mockFileData)))
                .andExpect(status().isBadRequest()).andReturn().getResponse().getContentAsString();

        APIResponse getAPIResponse = mapper.readValue(getData, APIResponse.class);
        assertEquals("Looks like one or more IBAN already in the system, please check the file again.", getAPIResponse.getApiResponse());
    }

    /**
     * Common method to generate file for running test cases
     * @param rawFileData
     * @return
    */
    private MockMultipartFile createMockFileForTest(String rawFileData) {
        String fileName = "mockfile.txt";
        return new MockMultipartFile(
                "file",
                fileName,
                "text/plain",
                rawFileData.getBytes()
        );
    }
}
package uk.gov.hmcts.reform.sscs.model;

import static org.junit.Assert.*;

import org.junit.Test;

public class AllowedFileTypesTest {

    @Test
    public void givenAFileWithWhitespace_thenTrimItOut() {
        String result = AllowedFileTypes.getContentTypeForFileName("myFile. pdf");

        assertEquals("application/pdf", result);
    }

}
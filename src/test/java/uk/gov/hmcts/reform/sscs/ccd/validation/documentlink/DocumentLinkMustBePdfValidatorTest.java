package uk.gov.hmcts.reform.sscs.ccd.validation.documentlink;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentLink;
import uk.gov.hmcts.reform.sscs.ccd.validation.ValidatorTestBase;

public class DocumentLinkMustBePdfValidatorTest extends ValidatorTestBase {

    private class TestBean {

        @DocumentLinkMustBePdf
        private DocumentLink testLink;

        @DocumentLinkMustBePdf(message = "Some Custom Message")
        private DocumentLink testLinkWithCustomMessage;
    }

    @Test
    public void testWhenDocumentLinkNotSet_IsValid() {

        TestBean testBean = new TestBean();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void testWhenDocumentLinkIsEmpty_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLink = DocumentLink.builder().build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Document link must be a PDF!");
    }

    @Test
    public void testWhenDocumentLinkIsHasOnlyUrl_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLink = DocumentLink.builder().documentUrl("someUrl").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Document link must be a PDF!");
    }

    @Test
    public void testWhenDocumentLinkIsHasOnlyValidFilename_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLink = DocumentLink.builder().documentFilename("somefile.pdf").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Document link must be a PDF!");
    }

    @Test
    public void testWhenDocumentLinkIsHasOnlyInvalidFilename_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLink = DocumentLink.builder().documentFilename("somefile.doc").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Document link must be a PDF!");
    }

    @Test
    public void testWhenDocumentLinkHasUrlAndInvalidFilename_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLink = DocumentLink.builder().documentUrl("someUrl").documentFilename("somefile.doc").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Document link must be a PDF!");
    }

    @Test
    public void testWhenDocumentLinkHasUrlAndInvalidFilename_WithCustomMessage_IsInvalid() {

        TestBean testBean = new TestBean();
        testBean.testLinkWithCustomMessage = DocumentLink.builder().documentUrl("someUrl").documentFilename("somefile.doc").build();

        Set<ConstraintViolation<TestBean>> violations = validator.validate(testBean);
        assertSingleViolationWithMessage(violations, "Some Custom Message");
    }

}

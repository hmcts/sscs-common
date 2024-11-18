package uk.gov.hmcts.reform.sscs.ccd.validation.documentlink;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.equalsAnyIgnoreCase;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import uk.gov.hmcts.reform.sscs.ccd.domain.DocumentLink;

public class DocumentLinkMustBePdfValidator implements ConstraintValidator<DocumentLinkMustBePdf, DocumentLink> {

    public static boolean isFileAPdf(DocumentLink docLink) {
        return docLink != null
            && isNotBlank(docLink.getDocumentUrl())
            && equalsAnyIgnoreCase("pdf", getExtension(docLink.getDocumentFilename()));
    }

    @Override
    public boolean isValid(DocumentLink documentLink, ConstraintValidatorContext context) {
        if (documentLink != null) {
            return isFileAPdf(documentLink);
        }
        return true;
    }
}

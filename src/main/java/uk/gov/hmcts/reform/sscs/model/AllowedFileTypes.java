package uk.gov.hmcts.reform.sscs.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import uk.gov.hmcts.reform.sscs.exception.UnknownFileTypeException;

@Data
@Builder
@Slf4j
public class AllowedFileTypes {
    private static final Map<String, String> ALLOWED_CONTENT_TYPES = new HashMap();

    static {
        ALLOWED_CONTENT_TYPES.put("doc", "application/msword");
        ALLOWED_CONTENT_TYPES.put("docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document");
        ALLOWED_CONTENT_TYPES.put("jpeg", "image/jpeg");
        ALLOWED_CONTENT_TYPES.put("jpg", "image/jpeg");
        ALLOWED_CONTENT_TYPES.put("pdf", "application/pdf");
        ALLOWED_CONTENT_TYPES.put("png", "image/png");
        ALLOWED_CONTENT_TYPES.put("ppt", "application/vnd.ms-powerpoint");
        ALLOWED_CONTENT_TYPES.put("pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation");
        ALLOWED_CONTENT_TYPES.put("txt", "text/plain");
        ALLOWED_CONTENT_TYPES.put("xls", "application/vnd.ms-excel");
        ALLOWED_CONTENT_TYPES.put("xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        ALLOWED_CONTENT_TYPES.put("bmp", "image/bmp");
        ALLOWED_CONTENT_TYPES.put("tiff", "image/tiff");
        ALLOWED_CONTENT_TYPES.put("tif", "image/tiff");
        ALLOWED_CONTENT_TYPES.put("dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template");
        ALLOWED_CONTENT_TYPES.put("xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template");
        ALLOWED_CONTENT_TYPES.put("potx", "application/vnd.openxmlformats-officedocument.presentationml.template");
        ALLOWED_CONTENT_TYPES.put("ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow");
        ALLOWED_CONTENT_TYPES.put("xlt", "application/vnd.ms-excel");
        ALLOWED_CONTENT_TYPES.put("dot", "application/msword");
        ALLOWED_CONTENT_TYPES.put("xla", "application/vnd.ms-excel");
        ALLOWED_CONTENT_TYPES.put("xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12");
        ALLOWED_CONTENT_TYPES.put("pot", "application/vnd.ms-powerpoint");
        ALLOWED_CONTENT_TYPES.put("pps", "application/vnd.ms-powerpoint");
        ALLOWED_CONTENT_TYPES.put("ppa", "application/vnd.ms-powerpoint");
    }

    private AllowedFileTypes() {
        // Private constructor
    }

    public static String getContentTypeForFileName(String fileName) {
        String fileTypeExtension = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();

        String trimmedExtension = StringUtils.trim(fileTypeExtension);
        if (ALLOWED_CONTENT_TYPES.containsKey(trimmedExtension)) {
            return ALLOWED_CONTENT_TYPES.get(trimmedExtension);
        } else {
            String message = "Evidence file type '" + fileTypeExtension + "' unknown";
            throw new UnknownFileTypeException(message, new Exception(message));
        }
    }
}

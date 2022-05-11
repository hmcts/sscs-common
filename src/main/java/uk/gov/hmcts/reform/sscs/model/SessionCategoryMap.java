package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import uk.gov.hmcts.reform.sscs.ccd.domain.BenefitCode;
import uk.gov.hmcts.reform.sscs.ccd.domain.Issue;
import uk.gov.hmcts.reform.sscs.ccd.domain.SessionCategory;
import uk.gov.hmcts.reform.sscs.exception.HearingDurationImportException;

@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@Slf4j
public class SessionCategoryMap {
    private BenefitCode benefitCode;
    private Issue issue;
    private boolean secondDoctor;
    private boolean fqpmRequired;
    private SessionCategory category;
    private Integer ticketOverride;

    private static final List<SessionCategoryMap> sessionCategoryMaps;

    private static final Map<Integer, SessionCategoryMap> BY_QUERY = new HashMap<>();

    private static final String SERVICE_CODE = "BBA3";

    public static final String JSON_DATA_LOCATION = "reference-data/session-category-map.json";

    static {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            sessionCategoryMaps = objectMapper.readValue(new ClassPathResource(JSON_DATA_LOCATION).getInputStream(), new TypeReference<List<SessionCategoryMap>>(){});
        } catch (IOException exception) {
            log.error("Error while reading SessionCategory from " + JSON_DATA_LOCATION + exception.getMessage(), exception);
            throw new HearingDurationImportException("Error while reading SessionCategory from " + JSON_DATA_LOCATION, exception);
        }
        for (SessionCategoryMap sessionCategoryMap : sessionCategoryMaps) {
            Integer hash = getQueryHash(sessionCategoryMap.benefitCode, sessionCategoryMap.issue, sessionCategoryMap.secondDoctor, sessionCategoryMap.fqpmRequired);
            BY_QUERY.put(hash, sessionCategoryMap);
        }
    }

    public static SessionCategoryMap getSessionCategory(String benefitCode, String issueCode, boolean secondDoctor, boolean fqpmRequired) {
        return getSessionCategory(BenefitCode.getBenefitCode(benefitCode), Issue.getIssue(issueCode), secondDoctor, fqpmRequired);
    }

    public static SessionCategoryMap getSessionCategory(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return BY_QUERY.get(getQueryHash(benefitCode, issue, secondDoctor, fqpmRequired));
    }

    public String getCategoryTypeValue() {
        return String.format("%s-%03d", SERVICE_CODE, benefitCode.getCcdReference());
    }

    public String getCategorySubTypeValue() {
        return String.format("%s%s", getCategoryTypeValue(), issue.name());
    }

    private static Integer getQueryHash(BenefitCode benefitCode, Issue issue, boolean secondDoctor, boolean fqpmRequired) {
        return Objects.hash(benefitCode, issue, secondDoctor, fqpmRequired);
    }
}

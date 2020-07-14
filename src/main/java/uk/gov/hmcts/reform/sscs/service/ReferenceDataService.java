package uk.gov.hmcts.reform.sscs.service;

import static uk.gov.hmcts.reform.sscs.model.RefKey.BAT_CODE_MAP;
import static uk.gov.hmcts.reform.sscs.model.RefKey.BEN_ASSESS_TYPE;
import static uk.gov.hmcts.reform.sscs.model.RefKey.CASE_CODE;
import static uk.gov.hmcts.reform.sscs.model.RefKey.FUR_EVID_TYPE;
import static uk.gov.hmcts.reform.sscs.model.RefKey.PTTP_ROLE;
import static uk.gov.hmcts.reform.sscs.model.RefKey.TRIBUNAL_TYPE;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BAT_CODE;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BENEFIT_DESC;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.BEN_ASSESS_TYPE_ID;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.FET_DESC;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.PTR_DESC;
import static uk.gov.hmcts.reform.sscs.model.RefKeyField.TBT_CODE;

import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.model.VenueDetails;
import uk.gov.hmcts.reform.sscs.repository.RefDataRepository;

@Service
@Slf4j
public class ReferenceDataService {

    private final Map<String, VenueDetails> venueDataMap;
    private RefDataRepository refDataRepo;

    @Autowired
    public ReferenceDataService(VenueDataLoader venueDataLoader) {
        this.venueDataMap = venueDataLoader.getVenueDetailsMap();
    }

    public VenueDetails getVenueDetails(String venueId) {
        return venueDataMap.get(venueId);
    }

    public String getBenefitType(String caseCodeId) {
        String benAssessType = refDataRepo.find(CASE_CODE, caseCodeId, BEN_ASSESS_TYPE_ID);
        String batCode = refDataRepo.find(BEN_ASSESS_TYPE, benAssessType, BAT_CODE);
        String benefitType;
        try {
            benefitType = refDataRepo.find(BAT_CODE_MAP, batCode, BENEFIT_DESC);
        } catch (Exception e) {
            log.debug("Oops...Not found benefitType for caseCodeId '" + caseCodeId
                + "', Benefit Type '" + benAssessType 
                + "', BAT Code '" + batCode + "'", e);
            return "ERR";
        }
        return benefitType;
    }

    public String getEvidenceType(String typeOfEvidenceId) {
        return refDataRepo.find(FUR_EVID_TYPE, typeOfEvidenceId, FET_DESC);
    }

    public String getRoleType(String roleId) {
        return refDataRepo.find(PTTP_ROLE, roleId, PTR_DESC);
    }

    public void setRefDataRepo(RefDataRepository repo) {
        this.refDataRepo = repo;
    }

    public String getTbtCode(String tribunalTypeId) {
        return refDataRepo.find(TRIBUNAL_TYPE, tribunalTypeId, TBT_CODE);
    }
}

package uk.gov.hmcts.reform.sscs.ccd.service;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import javax.annotation.Nullable;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdCallbackMap;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseDetails;
import uk.gov.hmcts.reform.sscs.idam.IdamService;


@Slf4j
@Service
@RequiredArgsConstructor
public class CcdCallbackMapService {
    private final CcdService ccdService;
    private final IdamService idamService;

    public SscsCaseData handleCcdCallbackMap(@Nullable CcdCallbackMap callbackMap, @Valid SscsCaseData caseData) {
        if (isNull(callbackMap)) {
            return caseData;
        }

        Long caseId = Long.valueOf(caseData.getCcdCaseId());

        if (nonNull(callbackMap.getPostCallbackDwpState())) {
            log.info("Setting DwpState to {} for case {}", callbackMap.getPostCallbackDwpState().getId(), caseId);
            caseData.setDwpState(callbackMap.getPostCallbackDwpState().getId());
        }

        if (nonNull(callbackMap.getCallbackEvent())) {
            SscsCaseDetails updatedCaseDetails = ccdService.updateCase(caseData, caseId,
                callbackMap.getCallbackEvent().getCcdType(), callbackMap.getCallbackSummary(),
                callbackMap.getCallbackDescription(), idamService.getIdamTokens());
            return updatedCaseDetails.getData();
        }
        return caseData;
    }

}

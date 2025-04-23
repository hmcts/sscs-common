package uk.gov.hmcts.reform.sscs.model.servicebus;

import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.ccd.domain.State;
import uk.gov.hmcts.reform.sscs.model.servicebus.SessionAwareRequest;

public interface SessionAwareMessagingService {

    boolean sendMessage(SessionAwareRequest message, SscsCaseData sscsCaseData, State caseState);

}

package uk.gov.hmcts.reform.sscs.model.servicebus;

import uk.gov.hmcts.reform.sscs.model.servicebus.SessionAwareRequest;

public class NoOpMessagingService implements SessionAwareMessagingService {

    @Override
    public boolean sendMessage(SessionAwareRequest message) {
        return true;
    }
}

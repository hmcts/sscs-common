package uk.gov.hmcts.reform.sscs.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uk.gov.hmcts.reform.sscs.ccd.callback.Callback;
import uk.gov.hmcts.reform.sscs.ccd.domain.SscsCaseData;
import uk.gov.hmcts.reform.sscs.client.CallbackOrchestratorApi;
import uk.gov.hmcts.reform.sscs.idam.IdamService;
import uk.gov.hmcts.reform.sscs.idam.IdamTokens;

@Service
@Slf4j
@RequiredArgsConstructor
public class CallbackOrchestratorService {
    private final CallbackOrchestratorApi callbackOrchestratorApi;
    private final IdamService idamService;

    @SuppressWarnings("unused")
    public void sendMessageToCallbackOrchestrator(Callback<SscsCaseData> callback) {
        IdamTokens idamTokens = idamService.getIdamTokens();

        log.info("Sending message to the callback orchestrator for the event {}", callback.getEvent());

        callbackOrchestratorApi.sendMessageToCallbackOrchestrator(
                idamTokens.getIdamOauth2Token(), idamTokens.getServiceAuthorization(), callback);
    }
}

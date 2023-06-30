package uk.gov.hmcts.reform.sscs.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    public HttpStatus sendMessageToCallbackOrchestrator(Callback<SscsCaseData> callback) {
        log.info("Sending message to the callback orchestrator for the event {}", callback.getEvent());
        IdamTokens idamTokens = idamService.getIdamTokens();

        try {
            ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
            String body = ow.writeValueAsString(callback);

            ResponseEntity<String> response = callbackOrchestratorApi.sendMessageToCallbackOrchestrator(
                    idamTokens.getIdamOauth2Token(), idamTokens.getServiceAuthorization(), body);

            return response.getStatusCode();
        } catch (JsonProcessingException e) {
            return HttpStatus.BAD_REQUEST;
        }
    }
}

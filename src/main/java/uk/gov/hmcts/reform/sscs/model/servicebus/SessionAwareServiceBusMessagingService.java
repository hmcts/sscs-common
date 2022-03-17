package uk.gov.hmcts.reform.sscs.model.servicebus;

import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import uk.gov.hmcts.reform.sscs.service.servicebus.SessionAwareMessagingService;

@Slf4j
public class SessionAwareServiceBusMessagingService implements SessionAwareMessagingService {

    private final ServiceBusSenderClient senderClient;

    public SessionAwareServiceBusMessagingService(
        ServiceBusSenderClient senderClient) {
        this.senderClient = senderClient;
    }

    @Override
    public boolean sendMessage(SessionAwareRequest message) {

        try {
            ObjectMapper objectMapper = new ObjectMapper();

            ServiceBusMessage serviceBusMessage = new ServiceBusMessage(objectMapper.writeValueAsString(message));
            serviceBusMessage.setSessionId(message.getSessionId());
            serviceBusMessage.setPartitionKey(message.getSessionId());

            log.info("About to send request with body: {}", serviceBusMessage.getBody().toString());

            senderClient.sendMessage(serviceBusMessage);

        } catch (Exception ex) {
            log.error("Unable to send message {}. Cause: {}", message, ex);

            return false;
        }

        return true;
    }
}

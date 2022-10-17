package uk.gov.hmcts.reform.sscs.idam;

import java.util.List;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class IdamTokens {
    String idamOauth2Token;
    String serviceAuthorization;
    final String userId;
    final String email;
    final List<String> roles;
}

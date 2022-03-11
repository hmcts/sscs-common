package uk.gov.hmcts.reform.sscs.model.client;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import java.io.Serializable;

@Value
@Builder
public class JudicialRefDataRequest implements Serializable {

    @NonNull String searchString;
    String serviceCode = "BBA3";
    String location;

}

package uk.gov.hmcts.reform.sscs.model.client;

import java.io.Serializable;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
public class JudicialRefDataRequest implements Serializable {

    @NonNull String searchString;
    String serviceCode = "BBA3";
    String location;

}

package uk.gov.hmcts.reform.sscs.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CaseWorkerProfilesDeletionResponse {

    private int statusCode;
    private String message;

}

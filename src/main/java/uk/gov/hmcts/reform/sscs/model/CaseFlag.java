package uk.gov.hmcts.reform.sscs.model;

import java.util.List;
import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Setter
public class CaseFlag {
    private List<Flag> flags;
}

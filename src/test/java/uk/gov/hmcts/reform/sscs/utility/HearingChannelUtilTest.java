package uk.gov.hmcts.reform.sscs.utility;

import org.junit.jupiter.api.Test;
import uk.gov.hmcts.reform.sscs.ccd.domain.CcdValue;
import uk.gov.hmcts.reform.sscs.ccd.domain.OtherParty;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class HearingChannelUtilTest {

    @Test
    void testNullHearingOptionsOtherParty(){
        CcdValue otherPartyList = CcdValue.builder()
                .value(OtherParty.builder().hearingOptions(null).build()).build();
        boolean result = HearingChannelUtil.isInterpreterRequiredOtherParties(List.of(otherPartyList));
        assertThat(result).isFalse();
    }
}

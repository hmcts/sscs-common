package uk.gov.hmcts.reform.sscs.ccd.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class SscsEsaCaseDataTest {

    private final SscsEsaCaseData data = new SscsEsaCaseData();

    @Test
    void shouldCreateDynamicEsaRegulationYears() {
        final DynamicRadioList dynamicList = data.defaultEsaRegulationsYears();

        assertThat(dynamicList.getListItems()).hasSize(2);
        assertThat(dynamicList.getListItems().getFirst().getCode()).isEqualTo("2008");
        assertThat(dynamicList.getListItems().getFirst().getLabel()).isEqualTo("2008");
        assertThat(dynamicList.getListItems().get(1).getCode()).isEqualTo("2013");
        assertThat(dynamicList.getListItems().get(1).getLabel()).isEqualTo("2013");
    }

    @ParameterizedTest
    @MethodSource("regulation35Selection")
    void shouldReturnWhetherRegulation35SelectionApplies(String schedule3ActivitiesApply, YesNo expected) {
        data.setDoesRegulation35Apply(YesNo.YES);
        data.setEsaWriteFinalDecisionSchedule3ActivitiesApply(schedule3ActivitiesApply);

        final YesNo regulation35Selection = data.getRegulation35Selection();

        assertThat(regulation35Selection).isEqualTo(expected);
    }

    @ParameterizedTest
    @MethodSource("schedule3Selection")
    void shouldReturnWhetherSchedule3SelectionApplies(String schedule3ActivitiesApply, List<String> expected) {
        data.setEsaWriteFinalDecisionSchedule3ActivitiesQuestion(List.of("test_data"));
        data.setEsaWriteFinalDecisionSchedule3ActivitiesApply(schedule3ActivitiesApply);

        final List<String> schedule3Selections = data.getSchedule3Selections();

        assertThat(schedule3Selections).isEqualTo(expected);
    }

    private static Stream<Arguments> regulation35Selection() {
        return Stream.of(
            Arguments.of("Yes", null),
            Arguments.of("No", YesNo.YES)
        );
    }

    private static Stream<Arguments> schedule3Selection() {
        return Stream.of(
            Arguments.of("Yes", List.of("test_data")),
            Arguments.of("No", List.of()),
            Arguments.of(null, null)
        );
    }

}
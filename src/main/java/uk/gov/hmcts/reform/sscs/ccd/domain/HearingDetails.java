package uk.gov.hmcts.reform.sscs.ccd.domain;

import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import uk.gov.hmcts.reform.sscs.model.client.JudicialUserBase;
import uk.gov.hmcts.reform.sscs.reference.data.model.HearingChannel;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder(toBuilder = true)
public class HearingDetails {
    private String hearingId;
    private String hearingDate;
    private String time;
    private String adjourned;
    private String eventDate;
    private Venue venue;
    private String venueId;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime hearingRequested;
    private Long versionNumber;
    private HearingStatus hearingStatus;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime start;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime end;
    private String epimsId;
    private HearingChannel hearingChannel;
    private JudicialUserBase judge;
    private List<CollectionItem<JudicialUserBase>> panelMembers;

    @SuppressWarnings("unused")
    @JsonIgnore
    public List<JudicialUserBase> getAllPanelMembers() {
        List<JudicialUserBase> allPanelMembers = new LinkedList<>();
        if (nonNull(panelMembers)) {
            allPanelMembers = panelMembers.stream().filter(Objects::nonNull).map(CollectionItem::getValue).toList();
        }

        if (nonNull(judge)) {
            allPanelMembers.add(0, judge);
        }

        return allPanelMembers.stream().filter(Objects::nonNull).toList();
    }

    @JsonIgnore
    public LocalDateTime getHearingDateTime() {
        if (isNotBlank(hearingDate) && isNotBlank(time)) {
            return LocalDateTime.of(LocalDate.parse(hearingDate), LocalTime.parse(time));
        }
        return null;
    }

    @SuppressWarnings("unused")
    @JsonIgnore
    public JudicialUserPanel getPanel() {
        if (panel == null) {
            this.panel = new JudicialUserPanel();
        }
        return panel;
    }
}

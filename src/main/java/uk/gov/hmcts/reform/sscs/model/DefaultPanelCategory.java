package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class DefaultPanelCategory {

    @JsonProperty("category_key")
    private String categoryKey;

    @JsonProperty("key")
    private String key;

    @JsonProperty("parent_category")
    private String parentCategory;

    @JsonProperty("parent_key")
    private String parentKey;

    @JsonProperty("external_reference_type")
    private String externalReferenceType;

    @JsonProperty("external_reference")
    private String externalReference;

}

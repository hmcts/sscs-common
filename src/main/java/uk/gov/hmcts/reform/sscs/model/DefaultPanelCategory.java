package uk.gov.hmcts.reform.sscs.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("external_reference_type")
    private String externalReferenceType;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("external_reference")
    private String externalReference;

}

package uk.gov.hmcts.reform.sscs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "feature.ibc-ni-postcodes")
public class VenueConfig {
    private boolean enableBelfast;

    public void setenableBelfast(boolean enableBelfast) {
        this.enableBelfast = enableBelfast;
    }

    public boolean enableBelfast() {
        return enableBelfast;
    }
}

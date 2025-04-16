package uk.gov.hmcts.reform.sscs.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "feature.ibc-ni-postcodes")
public class AirLookupConfig {
    private boolean allowNIPostcodes;

    public void setAllowNIPostcodes(boolean allowNIPostcodes) {
        this.allowNIPostcodes = allowNIPostcodes;
    }

    public boolean allowNIPostcodes() {
        return allowNIPostcodes;
    }
}

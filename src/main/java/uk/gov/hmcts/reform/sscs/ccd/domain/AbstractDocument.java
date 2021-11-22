package uk.gov.hmcts.reform.sscs.ccd.domain;

import com.fasterxml.jackson.annotation.*;
import lombok.experimental.SuperBuilder;
import org.apache.commons.collections4.ComparatorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.CompareToBuilder;
import org.apache.commons.lang3.math.NumberUtils;

@SuperBuilder(toBuilder = true)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AbstractDocument<D extends AbstractDocumentDetails> implements Comparable<AbstractDocument> {

    private D value;

    @JsonCreator
    public AbstractDocument(@JsonProperty("value") D value) {
        this.value = value;
    }

    public D getValue() {
        return value;
    }

    public void setValue(D value) {
        this.value = value;
    }

    @Override
    public int compareTo(AbstractDocument doc2) {
        return new CompareToBuilder()
                .appendSuper(firstCompareWithBundleLetter(doc2))
                .appendSuper(secondCompareWithBundleNumbers(doc2))
                .append(doc2.getValue().getDateTimeFormatted(), this.value.getDateTimeFormatted())
                .toComparison();
    }

    private int firstCompareWithBundleLetter(AbstractDocument doc2) {
        String o1 = this.getValue().getBundleAddition();
        String o2 = doc2.getValue().getBundleAddition();
        if (StringUtils.isNotEmpty(o1) && StringUtils.isNotEmpty(o2)) {
            String a1 = o1.substring(0,1);
            String a2 = o2.substring(0,1);
            return ComparatorUtils.<String>naturalComparator().compare(a1, a2);
        }
        return 0;
    }

    private int secondCompareWithBundleNumbers(AbstractDocument doc2) {
        String o1 = this.getValue().getBundleAddition();
        String o2 = doc2.getValue().getBundleAddition();
        if (StringUtils.isNotEmpty(o1) && StringUtils.isNotEmpty(o2)) {
            Integer n1 = NumberUtils.isCreatable(o1.substring(1)) ? Integer.parseInt(o1.substring(1)) : 0;
            Integer n2 = NumberUtils.isCreatable(o2.substring(1)) ? Integer.parseInt(o2.substring(1)) : 0;
            return ComparatorUtils.<Integer>naturalComparator().compare(n1, n2);
        }
        return 0;
    }
}

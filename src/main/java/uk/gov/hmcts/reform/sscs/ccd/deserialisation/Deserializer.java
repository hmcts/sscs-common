package uk.gov.hmcts.reform.sscs.ccd.deserialisation;

public interface Deserializer<T> {

    T deserialize(String source);
}

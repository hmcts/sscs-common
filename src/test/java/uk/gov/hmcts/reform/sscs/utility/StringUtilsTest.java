package uk.gov.hmcts.reform.sscs.utility;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

    @Test
    public void testEmptyList() {
        String result = StringUtils.getGramaticallyJoinedStrings(new ArrayList<>());
        assertEquals("", result);
    }

    @Test
    public void testSingleValuedList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one"));
        assertEquals("one", result);
    }

    @Test
    public void testTwoValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two"));
        assertEquals("one and two", result);
    }

    @Test
    public void testThreeValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two", "three"));
        assertEquals("one, two and three", result);
    }

    @Test
    public void testFourValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two", "three", "four"));
        assertEquals("one, two, three and four", result);
    }

}

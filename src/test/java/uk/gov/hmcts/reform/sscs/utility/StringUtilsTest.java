package uk.gov.hmcts.reform.sscs.utility;

import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

    @Test
    public void testEmptyList() {
        String result = StringUtils.getGramaticallyJoinedStrings(new ArrayList<>());
        Assert.assertEquals("", result);
    }

    @Test
    public void testSingleValuedList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one"));
        Assert.assertEquals("one", result);
    }

    @Test
    public void testTwoValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two"));
        Assert.assertEquals("one and two", result);
    }

    @Test
    public void testThreeValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two", "three"));
        Assert.assertEquals("one, two and three", result);
    }

    @Test
    public void testFourValuesInList() {
        String result = StringUtils.getGramaticallyJoinedStrings(Arrays.asList("one", "two", "three", "four"));
        Assert.assertEquals("one, two, three and four", result);
    }

    @Test
    public void testEmailIsMasked(){
        String result = StringUtils.getMaskedEmail("Joe.Bloggs@gmail.com");
        Assert.assertEquals("Joe***@gm***", result);
    }

    @Test
    public void testMobileIsMasked(){
        String result = StringUtils.getMaskedMobile("+447113653982");
        Assert.assertEquals("***3982", result);
    }

}

package uk.gov.hmcts.reform.sscs.utility;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SurnameUtilTest {

    @Test
    public void shouldMatchIfFollowedByBracketedAppointee() {
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith (Appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith (appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith ( Appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith ( appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith (Appointee )"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith (appointee )"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith ( Appointee )"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith ( appointee )"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith(Appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith", "Smith(appointee)"));
        Assert.assertTrue(SurnameUtil.compare("Smith (Appointee)", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith (appointee)", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith ( Appointee)", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith ( appointee)", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith (Appointee )", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith (appointee )", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith ( Appointee ) ", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith ( appointee )", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith(Appointee)", "Smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith(appointee)", "Smith"));
    }

    @Test
    public void shouldNotMatchIfFollowedByBracketedContentThatIsNotAppointee() {
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith (abcdedf)"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith ( abcdedf)"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith ( abcdedf)"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith (abcdedf )"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith ( abcdedf )"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith (  abcdedf)"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith(abcdedf)"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Smith( abcdedf )"));
    }

    @Test
    public void shouldNotMatchIfSurnamesAreDifferent() {
        Assert.assertFalse(SurnameUtil.compare("Smith", "Jones"));
        Assert.assertFalse(SurnameUtil.compare("Smith", "Jones (Appointee)"));
        Assert.assertFalse(SurnameUtil.compare("Smith (Appointee)", "Smythe"));
    }

    @Test
    public void shouldMatchIfTheSurnameIsActuallyAppointee() {
        Assert.assertTrue(SurnameUtil.compare("Appointee", "appointee"));
        Assert.assertTrue(SurnameUtil.compare("Appointee", "Appointee (Appointee)"));
    }

    @Test
    public void shouldIgnoreCaseAndSpaces() {
        Assert.assertTrue(SurnameUtil.compare("Smith", " smith"));
        Assert.assertTrue(SurnameUtil.compare("Smith", " smith "));
        Assert.assertTrue(SurnameUtil.compare("smith", "SMITH "));
    }

    @Test
    public void shouldIgnoreAccents() {
        Assert.assertTrue(SurnameUtil.compare("Tést", " test"));
        Assert.assertTrue(SurnameUtil.compare("Test", " Tést "));
        Assert.assertTrue(SurnameUtil.compare("smíth", "SMITH "));

        Assert.assertFalse(SurnameUtil.compare("smíth", "SMITHé "));
    }

    @Test(expected = NullPointerException.class)
    public void shouldFailOnNull() {
        SurnameUtil.compare(null, null);
    }

}
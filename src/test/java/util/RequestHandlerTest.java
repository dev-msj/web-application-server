package util;

import org.junit.Assert;
import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestHandlerTest {
    @Test
    public void testCheckQueryStringExist() {
        String queryString = "userId=asdf&password=asdf&name=adsf&email=asdf%40asdf.com";
        Pattern pattern = Pattern.compile("([^\\?]+)(\\?.*)?");
        Matcher m = pattern.matcher(queryString);

        Assert.assertTrue(m.matches());
    }
}

package util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class RequestPathHandlerTest {
    private final RequestPathHandler handler = new RequestPathHandler();

    @Test
    public void findPathTest() {
        String http = "http://test.co.kr:8080/my/test/path.html";
        Assert.assertThat(handler.getPath(http), CoreMatchers.is("my/test/path.html"));

        String https = "https://aha.com/take/on/me.jsp";
        Assert.assertThat(handler.getPath(https), CoreMatchers.is("take/on/me.jsp"));
    }
}

package util;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

public class RequestPathHandlerTest {
    private final RequestPathHandler handler = new RequestPathHandler();
    private final String uri = "http://localhost:8080/index.html";

    @Test
    public void findPathTest() {
        String http = "http://test.co.kr:8080/my/test/path.html";
        Assert.assertEquals("my/test/path.html", handler.getPath(http));

        String https = "https://aha.com/take/on/me.jsp";
        Assert.assertEquals("take/on/me.jsp", handler.getPath(https));
    }

    @Test
    public void findFileTest() {
        Assert.assertTrue(handler.isExistPath(uri));
    }

    @Test
    public void getFileData() {
        Assert.assertNotEquals("IOException", handler.readData(uri));
    }
}

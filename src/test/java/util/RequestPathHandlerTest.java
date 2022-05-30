package util;

import org.junit.Assert;
import org.junit.Test;

public class RequestPathHandlerTest {
    private final RequestPathHandler handler = new RequestPathHandler();
    private final String uri = "http://localhost:8080/index.html";

    @Test
    public void findFileTest() {
        Assert.assertTrue(handler.isExistPath(uri));
    }

    @Test
    public void getFileData() {
        Assert.assertNotEquals("IOException", handler.readData(uri));
    }
}

package util;

import org.junit.Assert;
import org.junit.Test;

public class RequestPathHandlerTest {
    private final RequestPathHandler handler = new RequestPathHandler();
    private final String path = "/index.html";

    @Test
    public void findFileTest() {
        Assert.assertTrue(handler.isExistPath(path));
    }

    @Test
    public void getFileData() {
        Assert.assertNotEquals("IOException", handler.readData(path));
    }
}

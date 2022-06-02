package webserver;

import java.io.*;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpResponseUtils;
import util.IOUtils;
import util.RequestPathHandler;

public class RequestHandler extends Thread {
    private static final Logger log = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        log.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            RequestPathHandler requestPathHandler = new RequestPathHandler();
            HttpResponseUtils httpResponseUtils = new HttpResponseUtils(out);
            String path = extractPath(in);

            if (requestPathHandler.isExistPath(path)) {
                byte[] data = requestPathHandler.readData(path);

                // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
                httpResponseUtils.setBody(data);
                httpResponseUtils.response200Header();
                httpResponseUtils.responseBody();

                return;
            }

            httpResponseUtils.setBody("페이지를 찾을 수 없습니다!".getBytes());
            httpResponseUtils.response404Header();
            httpResponseUtils.responseBody();
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    private String extractPath(InputStream in) throws IOException {
        String request = IOUtils.readData(new BufferedReader(new InputStreamReader(in)), in.available());

        return isEmptyRequest(request) ? "" : getPathString(request);
    }

    private String getPathString(String request) {
        String path = request.split("\n")[0].split(" ")[1];

        return isDefaultPath(path) ? "/index.html" : path;
    }

    private boolean isEmptyRequest(String request) {
        return request.length() == 0;
    }

    private boolean isDefaultPath(String path) {
        return path.equals("/");
    }
}

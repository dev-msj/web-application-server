package webserver;

import java.io.*;
import java.net.Socket;
import java.util.Map;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
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

            String[] requestInfo = extractRequestInfo(in);
            String httpMethod = requestInfo[0];
            String path = requestInfo[1];

            if (isGet(httpMethod)) {
                handleQueryString(path);
            } else if (isPost(httpMethod)) {
                
            } else {
                throw new RuntimeException("잘못된 요청입니다.");
            }

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

    private String[] extractRequestInfo(InputStream in) throws IOException {
        return new BufferedReader(new InputStreamReader(in)).readLine().split(" ");
    }

    private boolean isGet(String httpMethod) {
        return "GET".equals(httpMethod);
    }

    private boolean isPost(String httpMethod) {
        return "POST".equals(httpMethod);
    }

    private void handleQueryString(String path) {
        String[] splitPath = path.split("\\?");
        if (splitPath.length > 1) {
            saveUser(splitPath);
        }
    }

    private void saveUser(String[] splitPath) {
        String queryString = splitPath[1];
        Map<String, String> userInfo = HttpRequestUtils.parseQueryString(queryString);
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email")
        );

        System.out.println("user.toString() = " + user.toString());
    }
}

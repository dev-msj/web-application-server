package webserver;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.HttpRequestUtils;
import util.HttpResponseUtils;
import util.RequestPathHandler;

import java.io.*;
import java.net.Socket;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

            if (isEmptyRequest(requestInfo)) {
                return;
            }

            String path = getPath(requestInfo).split("\\?")[0];

            if (path.equals("/user/create")) {
                handleQueryString(requestInfo);
            }

            if (requestPathHandler.isExistPath(path)) {
                byte[] data = requestPathHandler.readData(path);

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

    private void handleQueryString(String[] requestInfo) {
        String httpMethod = getHttpMethod(requestInfo);
        String queryString = "";

        if (isGet(httpMethod)) {
            String[] splitPath = getPath(requestInfo).split("\\?");
            if (checkQueryStringExist(splitPath)) {
                queryString = splitPath[1];
            }
        } else if (isPost(httpMethod)) {
            queryString = requestInfo[requestInfo.length - 1];
        } else {
            throw new RuntimeException("잘못된 요청입니다.");
        }

        if (checkQueryString(queryString)) {
            saveUser(queryString);
        }
    }

    private boolean checkQueryStringExist(String[] splitPath) {
        return splitPath.length > 1;
    }

    private String[] extractRequestInfo(InputStream in) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
        StringBuilder stringBuilder = new StringBuilder();

        while (bufferedReader.ready()) {
            stringBuilder.append(bufferedReader.readLine()).append("\n");
        }

        return stringBuilder.toString().split("\n");
    }

    private boolean isEmptyRequest(String[] requestInfo) {
        return requestInfo.length == 1 && requestInfo[0].equals("");
    }

    private String getPath(String[] requestInfo) {
        return requestInfo[0].split(" ")[1];
    }

    private String getHttpMethod(String[] requestInfo) {
        return requestInfo[0].split(" ")[0];
    }

    private boolean isGet(String httpMethod) {
        return "GET".equals(httpMethod);
    }

    private boolean isPost(String httpMethod) {
        return "POST".equals(httpMethod);
    }

    private boolean checkQueryString(String queryString) {
        Pattern pattern = Pattern.compile("([^\\?]+)(\\?.*)?");
        Matcher m = pattern.matcher(queryString);

        return m.matches();
    }

    private void saveUser(String queryString) {
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

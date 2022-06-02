package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RequestPathHandler {
    private final String defaultPath = "./webapp";

    public boolean isExistPath(final String path) {
        File file = new File(defaultPath + path);

        return file.exists() && file.isFile();
    }

    public String readData(final String path) {
//        TODO: html, script 외의 파일을 열 때는 string으로 가져오면 에러가 나므로 byte타입 형태로 가져오게 변경 필요.
        try(Stream<String> stream = Files.lines(Paths.get(defaultPath + path))) {
            return parseDataToString(stream);
        } catch (IOException e) {
            System.out.println(e.toString());

            return "IOException";
        }
    }

    private String parseDataToString(final Stream<String> stream) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        stream.forEach(line -> stringBuilder.append(line).append("\n"));

        return stringBuilder.toString();
    }
}

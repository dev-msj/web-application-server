package util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class RequestPathHandler {
    private final String defaultPath = "./webapp/";

    public boolean isExistPath(final String path) {
        return new File(defaultPath + path).exists();
    }

    public String readData(final String path) {
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

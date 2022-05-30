package util;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class RequestPathHandler {
    private final String pattern = "(http|https)://(.*?)/(.*)";

    public String getPath(final String uri) {
        Matcher matcher = Pattern.compile(pattern).matcher(uri);
        if (matcher.find()) {
            return matcher.group(3);
        }

        return "";
    }

    public boolean isExistPath(final String uri) {
        return new File("./webapp/" + getPath(uri)).exists();
    }

    public String readData(final String uri) {
        try(Stream<String> stream = Files.lines(Paths.get("./webapp/" + getPath(uri)))) {
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

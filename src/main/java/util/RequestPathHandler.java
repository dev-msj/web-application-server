package util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RequestPathHandler {
    private final String pattern = "(http|https)://(.*?)/(.*)";

    public String getPath(final String uri) {
        Matcher matcher = Pattern.compile(pattern).matcher(uri);
        if (matcher.find()) {
            return matcher.group(3);
        }

        return "";
    }
}

package benchmark.escaped;

public class StringUtilsEscaped {

    private static final String LINE_SEPARATOR = "\n";

    public static String escapeControlCharacters(String value) {
        if (value.contains(LINE_SEPARATOR)) {
            switch (LINE_SEPARATOR) {
                case "\r\n":
                    return value.replaceAll(LINE_SEPARATOR, " \\\\r\\\\n ");
                case "\n":
                    return value.replaceAll(LINE_SEPARATOR, " \\\\n ");
                case "\r":
                    return value.replaceAll(LINE_SEPARATOR, " \\\\r ");
                default:
                    return value;
            }
        }
        return value;
    }
}

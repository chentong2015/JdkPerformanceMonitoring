package escaped.cases;

public class StringAPIEscaped {

    private static final String LINE_SEPARATOR = "\n";

    // TODO. String.replaceAll() 方法在某些场景不够高效
    public static String escapeControlLine(String value) {
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

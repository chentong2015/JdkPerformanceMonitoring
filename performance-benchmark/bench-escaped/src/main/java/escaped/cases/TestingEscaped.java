package escaped.cases;

import java.io.IOException;
import java.io.Writer;

public class TestingEscaped {

    public static void escapeLineControls(final String value, final Writer out) throws IOException {
        int start = 0;
        int current = 0;
        char[] input = value.toCharArray();
        while (current < value.length()) {
            int cp = input[current] & 0xFFFF;
            // Search for character needing escaping
            if (cp < 0x20 || cp == 0x22 || cp == 0x5C || cp >= 0x80) {
                out.write(value, start, current - start);
                switch (cp) {
                    case '"' -> out.write("\\\"");
                    case '\\' -> out.write("\\\\");
                    case '\b' -> out.write("\\b");
                    case '\f' -> out.write("\\f");
                    case '\n' -> out.write("\\n"); // OS系统的字符串换行符号
                    case '\r' -> out.write("\\r");
                    case '\t' -> out.write("\\t");
                    case '\0' -> out.write("\\0");
                    default -> escapeLineControlsByDefault(cp, out);
                }
                start = current + 1;
            }
            ++current;
        }
        // Write remaining chars.
        if (start != current) {
            out.write(value, start, current - start);
        }
        out.write('"');
    }

    private static void escapeLineControlsByDefault(int cp, final Writer out) throws IOException {
        if (isUCSScalar(cp)) {
            out.write("\\u");
        } else {
            out.write("\\x");
        }

        String hex = Integer.toHexString(cp);
        for (int i = hex.length(); i < 4; ++i) {
            out.write('0');
        }
        out.write(hex);
    }

    static boolean isUCSScalar(int cp) {
        // valid range is [0, 10FFFF].
        // scalar is anything that is not a surrogate.
        return !isSurrogate(cp);
    }

    static boolean isSurrogate(int cp) {
        // surrogate range is [D800, DFFF].
        return cp >= 0xD800 && cp <= 0xDFFF;
    }
}

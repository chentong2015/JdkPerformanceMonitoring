package benchmark.escaped;

import java.io.IOException;
import java.io.Writer;

public class WriterEscaped extends Writer {

    private final Writer underlying;

    public WriterEscaped(Writer underlying) {
        this.underlying = underlying;
    }

    public void writeEscaped(String value) throws IOException {
        if (value == null || value.isEmpty()) {
            return;
        }

        char[] chars = value.toCharArray();
        int length = chars.length;
        int start = 0;
        int current = 0;
        for (; current < length; ) {
            char c = chars[current];
            // Search for character needing escaping
            if (c < 0x20 || c == 0x22 || c == 0x5C) {
                // What we have so far does not contains anything to escape, write them all in once.
                underlying.write(chars, start, current - start);
                switch (c) {
                    case '"':
                        underlying.write("\\\"");
                        break;
                    case '\\':
                        underlying.write("\\\\");
                        break;
                    case '\b':
                        underlying.write("\\b");
                        break;
                    case '\f':
                        underlying.write("\\f");
                        break;
                    case '\n':
                        underlying.write("\\n");
                        break;
                    case '\r':
                        underlying.write("\\r");
                        break;
                    case '\t':
                        underlying.write("\\t");
                        break;
                    case '\0':
                        underlying.write("\\0");
                        break;
                    default:
                        underlying.write("u" + Integer.toString(c));
                        break;
                }
                start = current + 1;
            }
            ++current;
        }
        if (current != start) {
            // Write remainings chars.
            underlying.write(chars, start, current - start);
        }
    }

    @Override
    public void write(char[] cbuf, int off, int len) throws IOException {
        underlying.write(cbuf, off, len);

    }

    @Override
    public void flush() throws IOException {
        underlying.flush();
    }

    @Override
    public void close() throws IOException {
        underlying.close();
    }
}

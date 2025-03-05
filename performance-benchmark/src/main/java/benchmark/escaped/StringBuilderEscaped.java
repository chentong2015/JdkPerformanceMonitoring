package benchmark.escaped;

public class StringBuilderEscaped {

    StringBuilder stringBuilder = new StringBuilder();

    public StringBuilderEscaped append(final CharSequence value) {
        stringBuilder.append(value);
        return this;
    }

    public StringBuilderEscaped append(int nb) {
        stringBuilder.append(nb);
        return this;
    }

    public StringBuilderEscaped appendEscaped(final String value) {
        int length = value.length();
        int start = 0;
        int current = 0;
        for (; current < length; ) {
            char c = value.charAt(current);
            // Search for character needing escaping
            if (c < 0x20 || c == 0x22 || c == 0x5C) {
                // What we have so far does not contain anything to escape, write them all in once.
                stringBuilder.append(value.substring(start, current));
                switch (c) {
                    case '"':
                        stringBuilder.append("\\\"");
                        break;
                    case '\\':
                        stringBuilder.append("\\\\");
                        break;
                    case '\b':
                        stringBuilder.append("\\b");
                        break;
                    case '\f':
                        stringBuilder.append("\\f");
                        break;
                    case '\n':
                        stringBuilder.append("\\n");
                        break;
                    case '\r':
                        stringBuilder.append("\\r");
                        break;
                    case '\t':
                        stringBuilder.append("\\t");
                        break;
                    case '\0':
                        stringBuilder.append("\\0");
                        break;
                    default:
                        stringBuilder.append("u" + Integer.toString(c));
                        break;
                }

                start = current + 1;
            }
            ++current;
        }
        if (start == 0) {
            return this;
        }
        // Write remainings chars.
        if (current != start) {
            stringBuilder.append(value.substring(start, current));
        }
        return this;
    }

    public String toString() {
        return stringBuilder.toString();
    }
}

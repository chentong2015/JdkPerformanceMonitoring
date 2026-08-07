package truncation.bytes;

public class TruncationBaseBytes2 {

    private static final int ONE_BYTE_RANGE_LAST_CODE_POINT = 0x007F;
    private static final int TWO_BYTES_RANGE_LAST_CODE_POINT = 0x07FF;
    private static final int SURROGATE_RANGE_FIRST_CODE_POINT = 0xD800;
    private static final int SURROGATE_RANGE_LAST_CODE_POINT = 0xDFFF;

    // TODO. 累计字符的长度直到超过限制
    public static String truncateUTF8(String originalValue, int maxBytes) {
        if (originalValue == null)
            return null;
        int size = 0;
        for (int i = 0; i < originalValue.length(); i++) {
            char currentChar = originalValue.charAt(i);

            int skipCharsCount = 0;
            int charSize;
            if (currentChar <= ONE_BYTE_RANGE_LAST_CODE_POINT) {
                charSize = 1;
            } else if (currentChar <= TWO_BYTES_RANGE_LAST_CODE_POINT) {
                charSize = 2;
            } else if (currentChar < SURROGATE_RANGE_FIRST_CODE_POINT) {
                charSize = 3;
            } else if (currentChar <= SURROGATE_RANGE_LAST_CODE_POINT) {
                // Java's UTF-8 encoder maps surrogate pairs as a single 4-byte sequence
                // instead of two 3-byte sequences (so we can ignore the next char).
                // If we ignore surrogate pairs, the output value can be shorter than it needs to be.
                charSize = 4;
                skipCharsCount = 1;
            } else {
                charSize = 3;
            }

            if (size + charSize > maxBytes) {
                return originalValue.substring(0, i);
            }
            size += charSize;
            i += skipCharsCount;
        }
        return originalValue;
    }
}

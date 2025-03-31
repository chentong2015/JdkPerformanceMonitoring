package seperator.cases;

import java.nio.charset.StandardCharsets;

public class TruncationWithSeparatorBytes {

    // TODO: 如果超过限定的字节长度，则需要按照分隔符来截取
    public static String truncate(String input, int maxByteLength, String separator) {
        byte[] inputBytes = input.getBytes(StandardCharsets.UTF_8);
        if (inputBytes.length <= maxByteLength) {
            return input;
        }
        String inputTruncated = truncateUtf8(inputBytes, maxByteLength);

        // TODO. O(M) 判断截取后子字符串后面接的是否为分隔符，如果是则直接返回
        int offset = 0;
        int rightIndex = inputTruncated.length();
        while (rightIndex + offset < input.length() && offset < separator.length()
                && input.charAt(rightIndex + offset) == separator.charAt(offset)) {
            offset++;
        }
        if (offset == separator.length()) {
            return inputTruncated;
        }

        // O(N+N) 两次循环，根据API分隔符截取子字符串
        int lastPosition = inputTruncated.lastIndexOf(separator);
        if (lastPosition > 0) {
            return inputTruncated.substring(0, lastPosition);
        }
        return inputTruncated;
    }

    // 0(N) 先截取特定字节长度的字符串
    private static String truncateUtf8(byte[] inputBytes, int maxByteLength) {
        int lastIndex = maxByteLength;
        while (lastIndex > 0 && isContinuation(inputBytes[lastIndex])) {
            lastIndex--;
        }
        return new String(inputBytes, 0, lastIndex);
    }

    private static boolean isContinuation(int c) {
        return (c & 0xc0) == 0x80;
    }
}

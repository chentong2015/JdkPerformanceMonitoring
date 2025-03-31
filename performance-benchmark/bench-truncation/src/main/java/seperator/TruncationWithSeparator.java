package seperator;

import java.nio.charset.StandardCharsets;

public class TruncationWithSeparator {

    public static void main(String[] args) {
        String input = "ABS / aas / aai";
        System.out.println(truncateWithSeparatorAPI(input, 10, "/"));
    }

    // TODO: 使用StringBuilder API来完成字符串的截取和拼接
    public static String truncateWithSeparatorAPI(String input, int maxByteLength, String separator) {
        StringBuilder result = new StringBuilder(input);
        while (shouldTruncateString(result.toString(), maxByteLength)) {
            int pos = result.lastIndexOf(separator);
            if (pos != -1) {
                result.delete(pos, result.length());
            } else {
                return null;
            }
        }
        return result.toString();
    }

    // TODO: 算法问题
    private String truncateWithSeparatorByte(String input, int maxByteLength, String separator) {
        return "";
    }

    private static boolean shouldTruncateString(String input, int maxByteLength) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return bytes.length > maxByteLength;
    }
}

package seperator.cases;

import java.nio.charset.StandardCharsets;

public class TruncationWithSeparatorAPI {

    // TODO: 直接使用StringBuilder API来完成字符串的截取和拼接
    public static String truncate(String input, int maxByteLength, String separator) {
        StringBuilder result = new StringBuilder(input);
        while (shouldTruncate(result.toString(), maxByteLength)) {
            int pos = result.lastIndexOf(separator);
            if (pos != -1) {
                result.delete(pos, result.length());
            } else {
                return null;
            }
        }
        return result.toString();
    }

    private static boolean shouldTruncate(String input, int maxByteLength) {
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        return bytes.length > maxByteLength;
    }
}

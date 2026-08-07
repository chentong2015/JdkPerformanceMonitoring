package truncation.bytes;

import java.nio.charset.StandardCharsets;

public class TruncationUtf8CodePoint {

    // TODO. 判断CodePoint联系性, 取字节数组中指定范围长度的字节
    public static TruncationUtf8Result truncateUtf8(String str, int maxByteLength) {
        byte[] utf8bytes = str.getBytes(StandardCharsets.UTF_8);
        if (utf8bytes.length <= maxByteLength) {
            return new TruncationUtf8Result(str, "", false);
        }

        int lastIndex = maxByteLength;
        while (lastIndex > 0 && isContinuation(utf8bytes[lastIndex])) {
            lastIndex--;
        }

        // 使用字节数组构建字符串
        String result = new String(utf8bytes, 0, lastIndex, StandardCharsets.UTF_8);
        String truncatedPart = new String(utf8bytes, lastIndex, utf8bytes.length - lastIndex, StandardCharsets.UTF_8);
        return new TruncationUtf8Result(result, truncatedPart, lastIndex < maxByteLength);
    }

    // TODO. 判断字节位置是否连续: 连续则不能截取，避免截断后会出现乱码 éé�
    private static boolean isContinuation(int c) {
        return (c & 0xc0) == 0x80;
    }


    // 面向对象设计: 封装字符串截取的结果
    public static class TruncationUtf8Result {

        private final String result;
        private final String truncatedPart;
        private final boolean isOverflow;

        public TruncationUtf8Result(String result, String truncatedPart, boolean isOverflow) {
            this.result = result;
            this.truncatedPart = truncatedPart;
            this.isOverflow = isOverflow;
        }

        public String getTruncatedResult() {
            return result;
        }

        public String getTruncatedPart() {
            return truncatedPart;
        }

        // 判断截取完的字符串是否溢出: 仍然超过最大允许字节长度
        public boolean isOverflowOrError() {
            return isOverflow;
        }

        // 判断是否截取的字符串是有意义的，如果只是截取字符串末尾的空格，则直接调用string..trim()
        public boolean isTruncatedPartMeaningful() {
            return !truncatedPart.trim().isEmpty();
        }
    }
}

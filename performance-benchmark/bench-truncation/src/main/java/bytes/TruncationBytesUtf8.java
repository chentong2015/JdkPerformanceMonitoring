package bytes;

import java.nio.charset.StandardCharsets;

public class TruncationBytesUtf8 {

    private static final int MAX_COLUMN_BYTE_LENGTH = 5;

    public static void main(String[] args) {
        String value = "ééé";
        System.out.println(value.length()); // 3个字符
        System.out.println(value.getBytes(StandardCharsets.UTF_8).length); // 6个字节

        byte[] buffer = value.getBytes(StandardCharsets.UTF_8);
        if (buffer.length > MAX_COLUMN_BYTE_LENGTH) {
            TruncatedUTF8Result truncatedString = truncateUtf8(buffer);
            System.out.println(truncatedString.getTruncatedResult());
            System.out.println(truncatedString.getTruncatedPart());
        }
    }

    // TODO. 通过字节数组来截取字符串, 只取字节数组中指定范围长度的字节
    public static TruncatedUTF8Result truncateUtf8(byte[] utf8bytes) {
        // 不超过最大的允许字节长度，则截取的部分为空
        if (utf8bytes.length <= MAX_COLUMN_BYTE_LENGTH) {
            String result = new String(utf8bytes, 0, utf8bytes.length, StandardCharsets.UTF_8);
            return new TruncatedUTF8Result(result, "", false);
        }

        // 最后位置的byte可能是一个char字符的一部分，需要整个字符截取
        int lastIndex = MAX_COLUMN_BYTE_LENGTH;
        while (lastIndex > 0 && isContinuation(utf8bytes[lastIndex])) {
            lastIndex--;
        }
        return new TruncatedUTF8Result(new String(utf8bytes, 0, lastIndex, StandardCharsets.UTF_8),
               new String(utf8bytes, lastIndex, utf8bytes.length - lastIndex, StandardCharsets.UTF_8),
                lastIndex < MAX_COLUMN_BYTE_LENGTH);
    }

    // TODO. 判断所在的字节位置是否是连续的
    //  如果连续则不能截取，否则字符截断后会出现乱码 éé�
    private static boolean isContinuation(int c) {
        return (c & 0xc0) == 0x80;
    }


    // 面向对象设计: 截取后的结果对象(包含结果数据和截取掉的数据)
    static class TruncatedUTF8Result {
        private final String result;
        private final String truncatedPart;
        private final boolean isOverflow;

        public TruncatedUTF8Result(String result, String truncatedPart, boolean isOverflow) {
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

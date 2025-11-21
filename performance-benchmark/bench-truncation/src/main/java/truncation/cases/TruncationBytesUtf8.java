package truncation.cases;

import java.nio.charset.StandardCharsets;

public class TruncationBytesUtf8 {

    // TODO. 通过字节数组来截取字符串, 只取字节数组中指定范围长度的字节
    public static TruncationUtf8Result truncateUtf8(String str, int maxByteLength) {
        byte[] utf8bytes = str.getBytes(StandardCharsets.UTF_8);
        if (utf8bytes.length <= maxByteLength) {
            String result = new String(utf8bytes, 0, utf8bytes.length, StandardCharsets.UTF_8);
            return new TruncationUtf8Result(result, "", false);
        }

        int lastIndex = maxByteLength;
        while (lastIndex > 0 && isContinuation(utf8bytes[lastIndex])) {
            lastIndex--;
        }

        // 面向对象设计: 使用字节数组构建字符串
        return new TruncationUtf8Result(new String(utf8bytes, 0, lastIndex, StandardCharsets.UTF_8),
               new String(utf8bytes, lastIndex, utf8bytes.length - lastIndex, StandardCharsets.UTF_8),
                lastIndex < maxByteLength);
    }

    // TODO. 判断字节位置是否连续: 连续则不能截取，避免截断后会出现乱码 éé�
    private static boolean isContinuation(int c) {
        return (c & 0xc0) == 0x80;
    }
}

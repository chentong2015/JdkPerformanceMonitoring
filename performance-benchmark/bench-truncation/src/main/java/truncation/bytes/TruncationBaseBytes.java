package truncation.bytes;

public class TruncationBaseBytes {

    // TODO. 从前往后构建每个字符，算法复杂度较差
    public static String truncateBytes(String s, int maxByteLength) {
        StringBuilder output = new StringBuilder();
        int currentLength = 0;
        for (int i = 0; i < s.length(); i++) {
            int byteLength = 0;
            char c = s.charAt(i);
            if (c <= 0x7f) {
                byteLength = 1;
            } else if (c <= 0x7ff) {
                byteLength = 2;
            } else if (c <= 0xd7ff) {
                byteLength = 3;
            } else if (c <= 0xdbff) {
                byteLength = 4;
            } else if (c <= 0xdfff) {
                byteLength = 0;
            } else if (c <= 0xffff) {
                byteLength = 3;
            }
            // 判断是否超过字节长度限制
            if (currentLength + byteLength > maxByteLength) {
                break;
            }
            output.append(c);
            currentLength += byteLength;
        }
        return output.toString();
    }
}

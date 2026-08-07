package truncation.bytes;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.*;

public class TruncationByteBuffer {

    // TODO. 对字符串编码超出的末尾字节进行截取, 再构建新的字符串
    public static String truncateWithByteBuffer(String value, int maxByteLength) {
        CharBuffer input = CharBuffer.wrap(value);

        // 使用ByteBuffer来构建字符串, 分配允许的最大字节长度
        ByteBuffer output = ByteBuffer.allocate(maxByteLength);

        // Encodes as many as characters with UTF8 encoding from the given input buffer to output buffer
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        encoder.onMalformedInput(CodingErrorAction.IGNORE);
        encoder.encode(input, output, true);

        // Finally flushes this encoder
        encoder.flush(output);

        return new String(output.array(), 0, output.position(), StandardCharsets.UTF_8);
    }

    // TODO. 测试验证逻辑正确性
    // Truncates a string to at most {@code maxBytes} bytes once encoded with the given charset,
    // without cutting a multi-byte character in half.
    public static String truncateByBytes(String input, int maxBytes) {
        if (input == null) {
            return null;
        }
        if (maxBytes <= 0) {
            return "";
        }
        byte[] bytes = input.getBytes(StandardCharsets.UTF_8);
        if (bytes.length <= maxBytes) {
            return input;
        }

        CharsetDecoder decoder = StandardCharsets.UTF_8.newDecoder()
                .onMalformedInput(CodingErrorAction.REPORT)
                .onUnmappableCharacter(CodingErrorAction.REPORT);
        ByteBuffer inBuffer = ByteBuffer.wrap(bytes);
        CharBuffer outBuffer = CharBuffer.allocate(maxBytes);

        for (int length = maxBytes; length > 0; length--) {
            inBuffer.clear();
            inBuffer.limit(length);
            outBuffer.clear();
            decoder.reset();

            CoderResult result = decoder.decode(inBuffer, outBuffer, true);
            if (result.isUnderflow()) {
                CoderResult flushResult = decoder.flush(outBuffer);
                if (flushResult.isUnderflow()) {
                    outBuffer.flip();
                    return outBuffer.toString();
                }
            }
            // error or overflow: the last byte(s) cut a character in half, retry shorter
        }
        return "";
    }
}

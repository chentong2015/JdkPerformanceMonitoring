package truncation.bytes;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.nio.charset.StandardCharsets;

public class TruncationByteBuffer {

    // TODO. 对字符串编码超出的末尾字节进行截取, 再构建新的字符串
    public static String truncateWithByteBuffer(String value, int maxByteLength) {
        CharBuffer input = CharBuffer.wrap(value);

        // TODO. 使用ByteBuffer来构建字符串, 分配允许的最大字节长度
        ByteBuffer output = ByteBuffer.allocate(maxByteLength);

        // Encodes as many as characters with UTF8 encoding from the given input buffer to output buffer
        CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
        encoder.onMalformedInput(CodingErrorAction.IGNORE);
        encoder.encode(input, output, true);

        // Finally flushes this encoder
        encoder.flush(output);

        return new String(output.array(), 0, output.position(), StandardCharsets.UTF_8);
    }
}

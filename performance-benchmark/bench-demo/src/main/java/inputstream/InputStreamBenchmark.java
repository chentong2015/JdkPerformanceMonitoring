package inputstream;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class InputStreamBenchmark {

    public String toString(InputStream inputStream) throws IOException {
        ByteArrayOutputStream result = new ByteArrayOutputStream();

        // 每次读取指定buffer字节长度的数据
        byte[] buffer = new byte[1024];
        int byteLength = inputStream.read(buffer);
        while (byteLength != -1) {
            result.write(buffer, 0, byteLength);
            byteLength = inputStream.read(buffer);
        }

        return result.toString(StandardCharsets.UTF_8);
    }
}

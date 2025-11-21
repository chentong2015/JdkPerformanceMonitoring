package truncation;

import org.openjdk.jmh.annotations.*;
import truncation.cases.TruncationBaseBytes;
import truncation.cases.TruncationByteBuffer;
import truncation.cases.TruncationUtf8CodePoint;
import truncation.cases.TruncationUtf8Result;

import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TruncationBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void benchTruncationBytesBuffer() {
        for (int index = 0; index < 1000; index++) {
            String str = "A陈ÃabcedefA陈Ãabcedefééé"; // 32 bytes
            String strTruncated = TruncationBaseBytes.truncateBytes(str, 27);
        }
    }

    @Benchmark
    public void benchTruncationBaseBytes() {
        for (int index = 0; index < 1000; index++) {
            String str = "A陈ÃabcedefA陈Ãabcedefééé"; // 32 bytes
            String strTruncated = TruncationByteBuffer.truncateWithByteBuffer(str, 27);
        }
    }

    @Benchmark
    public void benchTruncationBytesUtf8() {
        for (int index = 0; index < 1000; index++) {
            String str = "A陈ÃabcedefA陈Ãabcedefééé"; // 32 bytes
            TruncationUtf8Result truncatedString = TruncationUtf8CodePoint.truncateUtf8(str, 27);
        }
    }
}

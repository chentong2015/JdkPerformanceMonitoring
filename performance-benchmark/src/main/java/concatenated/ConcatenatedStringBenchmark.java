package concatenated;

import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@BenchmarkMode(Mode.Throughput)
public class ConcatenatedStringBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void benchmarkStringConcat() {
        String str = "";
        for (int index = 0; index < 1000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                str.concat("a" + offset);
            }
        }
    }

    // StringBuffer is thread-safe
    @Benchmark
    public void benchmarkStringBuffer() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int index = 0; index < 1000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                stringBuffer.append("a" + offset);
            }
        }
    }

    @Benchmark
    public void benchmarkStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < 1000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                stringBuilder.append("a" + offset);
            }
        }
    }
}

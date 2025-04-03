package concatenated;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class ConcatenatedStringBenchmark {

    // TODO. 生成特定的JSON格式报告，用于可视化
    // Benchmark result is saved to StringBenchmark.json
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(ConcatenatedStringBenchmark.class.getSimpleName())
                .result("StringBenchmark.json")
                .resultFormat(ResultFormatType.JSON)
                .build();
        new Runner(opt).run();
    }

    @Benchmark
    public void benchmarkStringConcat() {
        String str = "";
        for (int index = 0; index < 2000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                str.concat("a" + offset);
            }
        }
    }

    // StringBuffer is thread-safe
    @Benchmark
    public void benchmarkStringBuffer() {
        StringBuffer stringBuffer = new StringBuffer();
        for (int index = 0; index < 2000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                stringBuffer.append("a" + offset);
            }
        }
    }

    @Benchmark
    public void benchmarkStringBuilder() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int index = 0; index < 2000; index++) {
            for (int offset = 0; offset < 26; offset++) {
                stringBuilder.append("a" + offset);
            }
        }
    }
}

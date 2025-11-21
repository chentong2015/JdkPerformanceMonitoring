package truncation;

import org.openjdk.jmh.annotations.*;
import truncation.seperator.TruncationWithSepAPI;
import truncation.seperator.TruncationWithSepBytes;
import java.util.concurrent.TimeUnit;

@Fork(value = 1)
@Warmup(iterations = 2)
@Measurement(iterations = 2)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class TruncationWithSepBenchmark {

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Benchmark
    public void benchTruncationWithSeparatorAPI() {
         for (int index = 0; index < 1000; index++) {
             String input = "ABS/aas/à/éé/ab/asa/:/:éééé://éé////ééééé////ééé///abac/////sdd///a/ah/sd/sdf/";
             String result1 = TruncationWithSepAPI.truncate(input, 9, "/");
         }
    }

    @Benchmark
    public void benchTruncationWithSeparatorBytes() {
        for (int index = 0; index < 1000; index++) {
            String input = "ABS/aas/à/éé/ab/asa/:/:éééé://éé////ééééé////ééé///abac/////sdd///a/ah/sd/sdf/";
            String result1 = TruncationWithSepBytes.truncate(input, 9, "/");
        }
    }
}

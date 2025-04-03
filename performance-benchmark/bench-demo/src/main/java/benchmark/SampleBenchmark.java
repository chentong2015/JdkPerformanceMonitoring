package benchmark;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.security.SecureRandom;
import java.util.concurrent.TimeUnit;

public class SampleBenchmark {

    // TODO. 执行当前类型中全部的@Benchmark
    public static void main(String[] args) throws Exception {
        Options opt = new OptionsBuilder()
                .include(SampleBenchmark.class.getSimpleName())
                .build();
        new Runner(opt).run();
    }

    // 作用域为每个Benchmark测试Case提供参数对象
    @State(Scope.Benchmark)
    public static class ExecutionPlan {
        @Param({ "10", "30"})
        public int iterations;

        public int value;
        public String name = "plan";

        @Setup(Level.Invocation)
        public void setUp() {
            value = new SecureRandom().nextInt(100);
        }
    }

    @Benchmark
    @Threads(1) // number of threads to run
    @Fork(value = 1, warmups = 1)
    @Warmup(iterations = 2)
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void sampleBenchmark(ExecutionPlan plan) {
        int resultValue = 0;
        StringBuilder finalName = new StringBuilder();
        for (int i = plan.iterations; i > 0; i--) {
            resultValue += i + plan.value;
            finalName.append(plan.name);
        }
    }
}

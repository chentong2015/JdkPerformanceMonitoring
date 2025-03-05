package jdk.jmh;

import org.openjdk.jmh.annotations.*;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

// 该类型中定义的Benchmark会逐个执行，最后得出结果
public class Benchmarks {

    // 默认模式是throughput吞吐量
    // Have an average time metric (instead of )
    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public void init() {
    }

    // 默认Fork次数为5次
    // value: how many times the benchmark will be executed,
    // warmups: how many times a benchmark will dry run before results 试运行/热身
    @Benchmark
    @Fork(value = 1, warmups = 2)
    @BenchmarkMode(Mode.Throughput)
    public void calculate() {

    }

    @Benchmark
    @Warmup(iterations = 5)
    public void test() {

    }

    @Benchmark
    @Fork(value = 1, warmups = 1)
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    @BenchmarkMode(Mode.Throughput)
    public void benchMurmur3_128(ExecutionPlan plan) {
        for (int i = plan.iterations; i > 0; i--) {
            plan.murmur3.putString(plan.password, Charset.defaultCharset());
        }
        plan.murmur3.hash();
    }
}

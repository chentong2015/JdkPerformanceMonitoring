package jdk.jmh;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;

import java.util.concurrent.TimeUnit;

public class BenchmarksElimination {

    // 0.34 ~ 0.35 ns/op
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void doNothing() {
    }

    // 在精确到纳秒级别上，对象的创建并不耗时 0.35 ~ 0.362 ns/op
    // creating and initializing an object is almost free!
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS)
    @BenchmarkMode(Mode.AverageTime)
    public void objectCreation() {
        new Object();
    }
}

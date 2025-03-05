package jdk.jmh;

import java.io.IOException;

// Java Microbenchmark Harness 构建/测试微纳秒级别的基准
// Building, running, and analysing nano/micro/milli/macro benchmarks
// written in Java and other languages targeting the JVM.

// https://github.com/openjdk/jmh
// https://www.baeldung.com/java-microbenchmark-harness
public class BenchmarkRunner {

    // Starts the benchmarking process
    public static void main(String[] args) throws IOException {
        org.openjdk.jmh.Main.main(args);
    }

    // # JMH version: 1.35
    // # VM version: JDK 15.0.2, OpenJDK 64-Bit Server VM, 15.0.2+7
    // # VM invoker: C:\jdk-15\bin\java.exe
    // # VM options: -javaagent:D:\Logiciel\..\IntelliJ IDEA 2021.3.1\bin -Dfile.encoding=UTF-8
    // # Blackhole mode: full + dont-inline hint (auto-detected, use -Djmh.blackhole.autoDetect=false to disable)
    // # Warmup: 5 iterations, 10 s each
    // # Measurement: 5 iterations, 10 s each
    // # Timeout: 10 min per iteration
    // # Threads: 1 thread, will synchronize iterations
    // # Benchmark mode: Throughput, ops/time
    // # Benchmark: com.jdk.jmh.Starter.init
    //
    // # Run progress: 0.00% complete, ETA 00:08:20
    // # Fork: 1 of 5
    // # Warmup Iteration   1: 2827205413.113 ops/s
    // # Warmup Iteration   2: 2881721160.881 ops/s
    // # Warmup Iteration   3: 2844285753.165 ops/s
    // # Warmup Iteration   4: 2878420125.355 ops/s
    // # Warmup Iteration   5: 2804070005.621 ops/s
    // Iteration   1: 2714849833.031 ops/s
    // Iteration   2: 2809989722.359 ops/s
    // Iteration   3: 2770016551.115 ops/s
    // Iteration   4: 2686248893.014 ops/s
    // Iteration   5: 2668312551.320 ops/s
    //
    // # Run progress: 20.00% complete, ETA 00:06:43
    // # Fork: 2 of 5
    // # Warmup Iteration   1:
    // # ...
}

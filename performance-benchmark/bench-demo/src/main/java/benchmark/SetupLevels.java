package benchmark;

import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Setup;

// TODO. 注意三种Setup级别
public class SetupLevels {

    // 运行基准测试前要执行的装置方法
    @Setup
    public void setup() {
        System.out.println("Fixture method to be run before the benchmark");
    }

    // Trial level: to be executed before/after each run of the benchmark.
    @Setup(Level.Trial)
    public void setupTrial() {
    }

    // Iteration level: to be executed before/after each iteration of the benchmark.
    @Setup(Level.Iteration)
    public void setupIteration() {
    }

    // Invocation level: to be executed for each benchmark method execution.
    @Setup(Level.Invocation)
    public void setupInvocation() throws IllegalAccessException, InstantiationException {
        System.out.println("invoke invocation");
    }
}

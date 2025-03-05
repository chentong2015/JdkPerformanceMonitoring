package jdk.jmh;

import com.google.common.hash.Hasher;
import com.google.common.hash.Hashing;
import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    // 设置执行的参数，每一次取其中一个数据
    // # Parameters: (iterations = 100)
    @Param({"100", "200", "300", "500", "1000"})
    public int iterations;

    public Hasher murmur3;
    public String password = "4v3rys3kur3p455w0rd";

    // The @Setup annotated method is invoked before each invocation of the benchmark
    @Setup(Level.Invocation)
    public void setUp() {
        murmur3 = Hashing.murmur3_128().newHasher();
    }
}

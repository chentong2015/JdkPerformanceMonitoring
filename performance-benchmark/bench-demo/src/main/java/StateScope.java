import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class StateScope {

    // TODO. @Param必须设置在@State注解的类型中
    // # Parameters: (iterations = 100) 设置执行迭代的次数
    @Param({"100", "200", "300", "500", "1000"})
    public int iterations;

    public String field1;
    public String field2 = "4v3rys3kur3p455w0rd";

    // The @Setup annotated method is invoked before each invocation of the benchmark
    @Setup(Level.Invocation)
    public void setUp() {
        field1 = "new field value";
    }
}

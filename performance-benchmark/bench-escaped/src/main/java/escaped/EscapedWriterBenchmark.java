package escaped;

import escaped.cases.StringBuilderEscaped;
import escaped.cases.StringAPIEscaped;
import escaped.cases.WriterEscaped;
import escaped.model.NullOutputStream;
import escaped.model.NullWriter;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Fork(value = 1)              // value表示正式测试次数，warmups表示正式测试前的热身次数
@Warmup(iterations = 2)       // iterations表示正式测试前的热身测试
@Measurement(iterations = 2)  // iterations表示正式次数的循环测试
@Timeout(time = 10)           // Timeout表示每个正式测试的时间限制
@State(Scope.Benchmark)       // Scope注明类属性应用的作用域区间
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class EscapedWriterBenchmark {

    private String value;
    private static final String LINE_SEPARATOR = "\n";
    private static final String DOUBLE_QUOTES = "\"\"\"";

    public static void main(String[] args) throws Exception {
        org.openjdk.jmh.Main.main(args);
    }

    @Setup
    public void setup() {
        value = RandomStringUtils.random(50, " abcdefghijklmnopqrstuvwxyz1234567890\n ");
    }

    @Benchmark
    public void testBaseWithoutEscape() throws IOException {
        testBaseWithoutEscape("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void testBaseWithoutEscape(final String tableName, final String columnName,
           String columnTypeName, int columnDisplaySize, String columnValue, String truncatedValue) throws IOException {

        StringBuilder warningBuilder = new StringBuilder();
        warningBuilder.append(tableName).append(",").append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .append(columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .append(truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append(LINE_SEPARATOR);

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new NullOutputStream());
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void testStringAPIEscaped() throws IOException {
        testStringAPIEscaped("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void testStringAPIEscaped(final String tableName, final String columnName,
           String columnTypeName, int columnDisplaySize, String columnValue, String truncatedValue) throws IOException {
        StringBuilder warningBuilder = new StringBuilder();
        String formattedSourceData = StringAPIEscaped.escapeControlLine(columnValue);
        String formattedTargetData = StringAPIEscaped.escapeControlLine(truncatedValue);

        warningBuilder.append(tableName).append(",")
                .append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .append(formattedSourceData).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .append(formattedTargetData).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append(LINE_SEPARATOR);

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new NullOutputStream());
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void testStringBuilderEscaped() throws IOException {
        testStringBuilderEscaped("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void testStringBuilderEscaped(final String tableName, final String columnName,
           String columnTypeName, int columnDisplaySize, String columnValue, String truncatedValue) throws IOException {
        StringBuilderEscaped warningBuilder = new StringBuilderEscaped();
        warningBuilder.append(tableName).append(",").append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .appendEscaped(columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .appendEscaped(truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append("\n");

        BufferedOutputStream bufferedOutput = new BufferedOutputStream(new NullOutputStream());
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void testWriterEscaped() throws IOException {
        testWriterEscaped("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void testWriterEscaped(final String tableName, final String columnName,
           String columnTypeName, int columnDisplaySize, String columnValue, String truncatedValue) throws IOException {
        WriterEscaped outputWriter = new WriterEscaped(new BufferedWriter(new NullWriter()));
        outputWriter.write(tableName);
        outputWriter.write(",");
        outputWriter.write(columnName);
        outputWriter.write(",");
        outputWriter.write(columnTypeName.toLowerCase());
        outputWriter.write("(");
        outputWriter.write(columnDisplaySize);
        outputWriter.write(")");
        outputWriter.write(",");
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.escapeControlLine(columnValue);
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.write(",");
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.escapeControlLine(truncatedValue);
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.write(",");
        outputWriter.write("Truncated");
        outputWriter.write("\n");
    }
}

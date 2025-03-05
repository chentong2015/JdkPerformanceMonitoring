package benchmark;

import benchmark.escaped.StringBuilderEscaped;
import benchmark.escaped.StringUtilsEscaped;
import benchmark.escaped.WriterEscaped;
import benchmark.model.NullOutputStream;
import benchmark.model.NullWriter;
import benchmark.model.TruncateInfos;
import org.apache.commons.lang3.RandomStringUtils;
import org.openjdk.jmh.annotations.*;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

@Fork(value = 1, warmups = 2) // warmups设置热身的次数
@Warmup(iterations = 2)       // default 20设置热身中循环的次数
@Timeout(time = 10)
@Measurement(iterations = 2)
@State(Scope.Benchmark)
public class OutputStreamBenchmarksTest {

    private static final String LINE_SEPARATOR = "\n";
    private static final String DOUBLE_QUOTES = "\"\"\"";

    private String value;
    private BufferedOutputStream bufferedOutput;
    private WriterEscaped outputWriter;

    // 在测试基准前后执行的配置和释放
    // 使用模拟出来的NullOutputStream和NullWriter
    @Setup
    public void setup() {
        value = RandomStringUtils.random(50, " abcdefghijklmnopqrstuvwxyz1234567890\n ");
        bufferedOutput = new BufferedOutputStream(new NullOutputStream());
        outputWriter = new WriterEscaped(new BufferedWriter(new NullWriter()));
        writeThread.start();
    }

    @TearDown
    public void tearDown() {
        lock.lock();
        notEmpty.signal();
        lock.unlock();
    }

    public synchronized void writeTruncationWarning(final String tableName, final String columnName,
                                                    final String columnTypeName, int columnDisplaySize,
                                                    String columnValue, String truncatedValue) throws IOException {
        StringBuilder warningBuilder = new StringBuilder();
        warningBuilder.append(tableName).append(",").append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .append(columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .append(truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append(LINE_SEPARATOR);
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void benchEscape_base() throws IOException {
        writeTruncationWarning("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void writeTruncationWarning_0(final String tableName, final String columnName,
                                                      final String columnTypeName, int columnDisplaySize,
                                                      String columnValue, String truncatedValue) throws IOException {
        StringBuilder warningBuilder = new StringBuilder();
        String formattedSourceData = StringUtilsEscaped.escapeControlCharacters(columnValue);
        String formattedTargetData = StringUtilsEscaped.escapeControlCharacters(truncatedValue);

        warningBuilder.append(tableName).append(",").append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .append(formattedSourceData).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .append(formattedTargetData).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append(LINE_SEPARATOR);
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void benchEscape_0() throws IOException {
        writeTruncationWarning_0("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void writeTruncationWarning_1(final String tableName, final String columnName,
                                                      final String columnTypeName, int columnDisplaySize,
                                                      String columnValue, String truncatedValue) throws IOException {
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
        outputWriter.writeEscaped(columnValue);
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.write(",");
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.writeEscaped(truncatedValue);
        outputWriter.write(DOUBLE_QUOTES);
        outputWriter.write(",");
        outputWriter.write("Truncated");
        outputWriter.write("\n");
    }

    @Benchmark
    public void benchEscape_1() throws IOException {
        writeTruncationWarning_1("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    public synchronized void writeTruncationWarning_2(final String tableName, final String columnName,
                                                      final String columnTypeName, int columnDisplaySize,
                                                      String columnValue, String truncatedValue) throws IOException {
        StringBuilderEscaped warningBuilder = new StringBuilderEscaped();

        warningBuilder.append(tableName).append(",").append(columnName).append(",").append(columnTypeName.toLowerCase())
                .append("(").append(columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                .appendEscaped(columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                .appendEscaped(truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                .append("\n");
        bufferedOutput.write(warningBuilder.toString().getBytes());
    }

    @Benchmark
    public void benchEscape_2() throws IOException {
        writeTruncationWarning_2("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }


    // 多线程的一个可重入锁
    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private ConcurrentLinkedDeque<TruncateInfos> warnings = new ConcurrentLinkedDeque<>();

    private Thread writeThread = new Thread(new Runnable() {
        @Override
        public void run() {
            lock.lock();
            try {
                notEmpty.await();
                TruncateInfos infos = warnings.poll();
                if (infos != null) {
                    StringBuilderEscaped warningBuilder = new StringBuilderEscaped();
                    warningBuilder.append(infos.tableName).append(",").append(infos.columnName).append(",")
                            .append(infos.columnTypeName.toLowerCase())
                            .append("(").append(infos.columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                            .appendEscaped(infos.columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                            .appendEscaped(infos.truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                            .append("\n");
                    bufferedOutput.write(warningBuilder.toString().getBytes());
                }
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    });

    public void writeTruncationWarning_3(final String tableName, final String columnName,
                                         final String columnTypeName, int columnDisplaySize,
                                         String columnValue, String truncatedValue) {
        lock.lock();
        warnings.push(new TruncateInfos(tableName, columnName, columnTypeName, columnDisplaySize, columnValue, truncatedValue));
        notEmpty.signal();
        lock.unlock();
    }

    @Benchmark
    public void benchEscape_3() throws IOException {
        writeTruncationWarning_3("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    // 测试当前的Benchmarks
    public static void main(String[] args) throws Exception {
        // String value = "abc\ndef";
        // System.out.println(writeTruncationWarning_0("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value));
        // System.out.println(writeTruncationWarning_1("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value));
        org.openjdk.jmh.Main.main(args);
    }
}

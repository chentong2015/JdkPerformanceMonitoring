package escaped;

import org.apache.commons.lang3.RandomStringUtils;
import escaped.cases.StringBuilderEscaped;
import escaped.model.NullOutputStream;
import escaped.model.TruncatedInfo;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EscapedWriterBenchmark2 {

    private String value;
    private BufferedOutputStream bufferedOutput;
    private static final String DOUBLE_QUOTES = "\"\"\"";

    private Lock lock = new ReentrantLock();
    private Condition notEmpty = lock.newCondition();
    private ConcurrentLinkedDeque<TruncatedInfo> infoDeque = new ConcurrentLinkedDeque<>();

    // TODO. 使用异步线程来执行IO数据输出
    private Thread writeThread = new Thread(new Runnable() {
        @Override
        public void run() {
            lock.lock();
            try {
                notEmpty.await();
                TruncatedInfo info = infoDeque.poll();
                StringBuilderEscaped warningBuilder = new StringBuilderEscaped();
                warningBuilder.append(info.tableName).append(",").append(info.columnName).append(",")
                        .append(info.columnTypeName.toLowerCase())
                        .append("(").append(info.columnDisplaySize).append(")").append(",").append(DOUBLE_QUOTES)
                        .appendEscaped(info.columnValue).append(DOUBLE_QUOTES).append(",").append(DOUBLE_QUOTES)
                        .appendEscaped(info.truncatedValue).append(DOUBLE_QUOTES).append(",").append("Truncated")
                        .append("\n");
                bufferedOutput.write(warningBuilder.toString().getBytes());
            } catch (InterruptedException | IOException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
    });

    // @Setup
    public void setup() {
        writeThread.start();
        value = RandomStringUtils.random(50, " abcdefghijklmnopqrstuvwxyz1234567890\n ");
        bufferedOutput = new BufferedOutputStream(new NullOutputStream());
    }

    // @Benchmark
    public void benchEscape_3() throws IOException {
        testEscapedWithMultiThread("ABC_DBF", "M_DESCRIPTION", "varchar", 255, value, value);
    }

    // 通过往双端队列中添加Info信息，激活并发队列的Condition信号
    public void testEscapedWithMultiThread(String tableName, final String columnName,
           String columnTypeName, int columnDisplaySize, String columnValue, String truncatedValue) {
        lock.lock();
        infoDeque.push(new TruncatedInfo(tableName, columnName, columnTypeName, columnDisplaySize, columnValue, truncatedValue));
        notEmpty.signal();
        lock.unlock();
    }

    // @TearDown
    public void tearDown() {
        lock.lock();
        notEmpty.signal();
        lock.unlock();
    }
}

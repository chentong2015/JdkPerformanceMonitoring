package benchmarks.model;

public class TruncatedInfo {

    public final String tableName;
    public final String columnName;
    public String columnTypeName;
    public int columnDisplaySize;
    public String columnValue;
    public String truncatedValue;

    public TruncatedInfo(String tableName, String columnName, String columnTypeName, int columnDisplaySize,
                         String columnValue, String truncatedValue) {
        this.tableName = tableName;
        this.columnName = columnName;
        this.columnTypeName = columnTypeName;
        this.columnDisplaySize = columnDisplaySize;
        this.columnValue = columnValue;
        this.truncatedValue = truncatedValue;
    }
}

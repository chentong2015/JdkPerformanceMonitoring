package truncation.cases;

public class TruncationUtf8Result {

    private final String result;
    private final String truncatedPart;
    private final boolean isOverflow;

    public TruncationUtf8Result(String result, String truncatedPart, boolean isOverflow) {
        this.result = result;
        this.truncatedPart = truncatedPart;
        this.isOverflow = isOverflow;
    }

    public String getTruncatedResult() {
        return result;
    }

    public String getTruncatedPart() {
        return truncatedPart;
    }

    // 判断截取完的字符串是否溢出: 仍然超过最大允许字节长度
    public boolean isOverflowOrError() {
        return isOverflow;
    }

    // 判断是否截取的字符串是有意义的，如果只是截取字符串末尾的空格，则直接调用string..trim()
    public boolean isTruncatedPartMeaningful() {
        return !truncatedPart.trim().isEmpty();
    }
}

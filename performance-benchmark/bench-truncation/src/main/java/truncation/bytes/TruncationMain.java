package truncation.bytes;

import java.nio.charset.StandardCharsets;

public class TruncationMain {

    public static void main(String[] args) {
        String value = "ပြည်ထောင်စု သမ္မတ မြန်မာနိုင်ငံတော်";
        System.out.println(value.getBytes(StandardCharsets.UTF_8).length);

        String result1 = TruncationBaseBytes2.truncateUTF8(value, 100);
        System.out.println(result1);
        System.out.println(result1.getBytes(StandardCharsets.UTF_8).length);

        TruncationCodePointUtf8.TruncationUtf8Result result2 = TruncationCodePointUtf8.truncateUtf8(value, 100);
        System.out.println(result2.getTruncatedResult());
        System.out.println(result2.getTruncatedResult().getBytes(StandardCharsets.UTF_8).length);
        System.out.println(result2.getTruncatedPart().getBytes(StandardCharsets.UTF_8).length);
    }
}

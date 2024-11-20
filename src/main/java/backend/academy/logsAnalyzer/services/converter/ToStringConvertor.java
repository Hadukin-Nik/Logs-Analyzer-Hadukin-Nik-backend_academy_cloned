package backend.academy.logsAnalyzer.services.converter;

import java.time.Instant;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

public class ToStringConvertor implements IToString {
    @Override
    public String convert(OffsetDateTime date) {
        if (date == null) {
            return "-";
        }

        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public String convert(Long number) {
        StringBuilder ans = new StringBuilder();

        int countOfAdded = 0;
        for (long i = number; i > 0; i /= 10) {
            ans.append(i % 10);

            countOfAdded++;

            if (countOfAdded % 3 == 0 && i > 9) {
                ans.append("_");
            }
        }

        for (int i = 0; i < ans.length() / 2; i++) {
            char buf = ans.charAt(i);

            ans.setCharAt(i, ans.charAt(ans.length() - 1 - i));
            ans.setCharAt(ans.length() - 1 - i, buf);
        }

        return ans.toString();
    }

    @Override
    public String convert(List<String> strings) {
        return strings.stream().map(s -> "'" + s + "'").collect(Collectors.joining(", "));
    }

    @Override
    public String convert(Instant instant) {
        return LocalTime.ofInstant(instant, ZoneId.systemDefault()).format(DateTimeFormatter.ISO_LOCAL_TIME);
    }

}

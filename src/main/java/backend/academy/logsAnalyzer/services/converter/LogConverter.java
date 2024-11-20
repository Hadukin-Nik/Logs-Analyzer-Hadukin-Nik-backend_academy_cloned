package backend.academy.logsAnalyzer.services.converter;

import backend.academy.logsAnalyzer.models.util.Log;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class LogConverter implements ILogFromStringConverter {
    @SuppressWarnings({"MagicNumber"})
    public Log convert(String s) {
        try {
            String[] s1 = s.replaceAll("\"", "").split(" ");

            String remoteAddress = s1[0];
            String remoteUser = s1[2];

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MMM/yyyy:HH:mm:ssZ", Locale.ENGLISH);
            StringBuilder sb = new StringBuilder(s1[3].substring(1) + s1[4].substring(0, s1[4].length() - 1));
            OffsetDateTime date =
                OffsetDateTime.parse(sb, dtf);

            String method = s1[5];

            String address = s1[6];
            String httpVersion = s1[7];

            short code = Short.parseShort(s1[8]);

            long bodyBytesSent = Long.parseLong(s1[9]);

            String httpReferer = s1[10];

            String userAgent =
                IntStream.range(11, s1.length).mapToObj(i -> s1[i].toUpperCase()).collect(Collectors.joining());

            return new Log(remoteAddress, remoteUser, date, method, address, httpVersion, code, bodyBytesSent,
                httpReferer, userAgent);
        } catch (RuntimeException e) {
            throw new RuntimeException(e);
        }

    }
}

package backend.academy.logsAnalyzer.services.converter;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;

public interface IToString {
    String convert(OffsetDateTime date);

    String convert(Long number);

    String convert(List<String> strings);

    String convert(Instant instant);
}

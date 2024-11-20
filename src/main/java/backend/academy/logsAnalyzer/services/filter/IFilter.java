package backend.academy.logsAnalyzer.services.filter;

import backend.academy.logsAnalyzer.models.util.Log;
import java.time.OffsetDateTime;

public interface IFilter {
    boolean filter(Log log);

    OffsetDateTime getFrom();

    OffsetDateTime getTo();
}

package backend.academy.logsAnalyzer.models.logic.output;

import java.time.OffsetDateTime;
import java.util.List;

public record ViewLogsAnalyzerAnswer(BasicLogsAnalyzerAnswer basicAnswer,
                                     List<String> files,
                                     OffsetDateTime from,
                                     OffsetDateTime to) {
}

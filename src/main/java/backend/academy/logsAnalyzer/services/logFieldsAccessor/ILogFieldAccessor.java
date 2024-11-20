package backend.academy.logsAnalyzer.services.logFieldsAccessor;

import backend.academy.logsAnalyzer.models.util.Log;
import java.util.Set;

public interface ILogFieldAccessor {
    Set<String> getFieldNames();

    String getFieldValueAsString(Log log, String fieldName);
}

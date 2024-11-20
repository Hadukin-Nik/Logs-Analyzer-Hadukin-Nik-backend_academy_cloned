package backend.academy.logsAnalyzer.services.logFieldsAccessor;

import backend.academy.logsAnalyzer.models.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class LogReflection implements ILogFieldAccessor {
    private final Map<String, RecordComponent> fields;

    public LogReflection() {
        fields = Arrays.stream(Log.class.getRecordComponents()).collect(Collectors.toMap(f -> f.getName().toUpperCase(),
            f -> f));
    }

    public Set<String> getFieldNames() {
        return fields.keySet();
    }

    public String getFieldValueAsString(Log log, String fieldName) {
        RecordComponent field = fields.getOrDefault(fieldName.toUpperCase(), null);
        if (field != null) {
            try {
                return field.getAccessor().invoke(log).toString();
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }
}

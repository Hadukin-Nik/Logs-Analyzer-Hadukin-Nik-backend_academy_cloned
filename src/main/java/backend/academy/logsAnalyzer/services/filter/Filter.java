package backend.academy.logsAnalyzer.services.filter;

import backend.academy.logsAnalyzer.models.util.Log;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.ILogFieldAccessor;
import java.time.OffsetDateTime;

public class Filter implements IFilter {
    private final ILogFieldAccessor logReflection;

    private OffsetDateTime from;
    private OffsetDateTime to;

    private String field;
    private String regexOfValue;

    public Filter(
        ILogFieldAccessor logReflection,
        OffsetDateTime from,
        OffsetDateTime to,
        String field,
        String regexOfValue
    ) {
        this.logReflection = logReflection;
        this.from = from;
        this.to = to;
        this.field = field;
        this.regexOfValue = regexOfValue;

        if (from != null && to != null && !(from.isBefore(to)) && !from.equals(to)) {
            throw new IllegalArgumentException("From and To don't match");
        }

        if (field != null && (regexOfValue == null || regexOfValue.isEmpty())) {
            throw new IllegalArgumentException("Containing string cannot be null or empty");
        }

        if (field != null) {
            this.field = field.toUpperCase();
            this.regexOfValue = regexOfValue.toUpperCase().replaceAll("\\*", ".*");
        }

        if (this.field != null && !this.field.isEmpty() && !logReflection.getFieldNames().contains(this.field)) {
            throw new IllegalArgumentException("Field " + field + " does not exist");
        }
    }

    @Override
    public boolean filter(Log log) {
        if (from != null && from.isAfter(log.date()) || to != null && to.isBefore(log.date())) {
            return false;
        }

        if (field != null && !logReflection.getFieldValueAsString(log, field).matches(regexOfValue)) {
            return false;
        }

        return true;
    }

    @Override
    public OffsetDateTime getFrom() {
        return from;
    }

    @Override
    public OffsetDateTime getTo() {
        return to;
    }
}

package backend.academy.logsAnalyzer.services.converter;

import backend.academy.logsAnalyzer.models.util.Log;

public interface ILogFromStringConverter {
    Log convert(String obj);
}

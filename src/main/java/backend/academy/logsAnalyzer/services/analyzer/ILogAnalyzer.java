package backend.academy.logsAnalyzer.services.analyzer;

import backend.academy.logsAnalyzer.models.logic.output.BasicLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.models.util.Log;

public interface ILogAnalyzer {
    void add(Log log);

    BasicLogsAnalyzerAnswer analyze();
}

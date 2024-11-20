package backend.academy.logsAnalyzer.services.output;

import backend.academy.logsAnalyzer.models.logic.output.ViewLogsAnalyzerAnswer;

public interface ILogicView {
    void print(ViewLogsAnalyzerAnswer report);
}

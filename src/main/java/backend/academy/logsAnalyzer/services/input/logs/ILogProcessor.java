package backend.academy.logsAnalyzer.services.input.logs;

import backend.academy.logsAnalyzer.services.analyzer.ILogAnalyzer;
import java.util.List;

public interface ILogProcessor {
    /**
     * adding all found logs from path to log analyzers, returns file names
     * @param iLogAnalyzer an Analyzer which will holds all collected data
     * @param path         a path where we start look on
     * @param limit        a limit on read logs per method
     * @return A list of used data, such as file names
     */
    List<String> addLogsToAnalyzer(ILogAnalyzer iLogAnalyzer, String path, int limit);
}

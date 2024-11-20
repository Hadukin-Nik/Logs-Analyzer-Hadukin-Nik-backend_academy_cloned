package backend.academy.logsAnalyzer.services.analyzer;

import backend.academy.logsAnalyzer.models.logic.output.BasicLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.models.util.Log;
import backend.academy.logsAnalyzer.services.filter.IFilter;
import backend.academy.logsAnalyzer.services.sketch.DataHolder;
import java.util.HashMap;
import java.util.Map;

public class LogAnalyzer implements ILogAnalyzer {
    private final IFilter filter;
    private final DataHolder sketchForBodyResponseSize;
    private final DataHolder sketchForDistancesBetweenLogs;
    private Map<String, Integer> resourcesUsed;
    private Map<Short, Integer> responsesCodes;

    private Log lastAdded;

    public LogAnalyzer(IFilter filter) {
        this.filter = filter;

        sketchForBodyResponseSize = new DataHolder();
        sketchForDistancesBetweenLogs = new DataHolder();

        resourcesUsed = new HashMap<>();
        responsesCodes = new HashMap<>();
    }

    @Override
    public void add(Log log) {
        if (lastAdded != null) {
            sketchForDistancesBetweenLogs.add(log.date().toEpochSecond() - lastAdded.date().toEpochSecond());
        }
        lastAdded = log;

        if (!filter.filter(log)) {
            return;
        }
        if (!resourcesUsed.containsKey(log.request())) {
            resourcesUsed.put(log.request(), 0);
        }
        resourcesUsed.put(log.request(), resourcesUsed.get(log.request()) + 1);

        if (!responsesCodes.containsKey(log.status())) {
            responsesCodes.put(log.status(), 0);
        }
        responsesCodes.put(log.status(), responsesCodes.get(log.status()) + 1);

        sketchForBodyResponseSize.add(log.bodyBytesSent());
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public BasicLogsAnalyzerAnswer analyze() {
        return new BasicLogsAnalyzerAnswer(resourcesUsed, responsesCodes, sketchForBodyResponseSize.getCount(),
            (long) sketchForBodyResponseSize.getAverage(), (long) sketchForBodyResponseSize.getQuantileValue(0.95),
            (long) sketchForBodyResponseSize.getQuantileValue(0.05),
            (long) sketchForBodyResponseSize.getQuantileValue(0.5),
            (long) sketchForDistancesBetweenLogs.getQuantileValue(0.5));
    }
}

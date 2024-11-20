package backend.academy.logsAnalyzer.models.logic.output;

import java.util.Map;

public record BasicLogsAnalyzerAnswer(Map<String, Integer> resourcesUsed,
                                      Map<Short, Integer> answerCodes,
                                      long responseCount,
                                      long averageResponseTime,
                                      long percentilel95ResponseSize,
                                      long percentile5ResponseSize,
                                      long median,
                                      long averageDownTime
) {

}

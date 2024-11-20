package backend.academy.logsAnalyzer.services.analyzer;

import backend.academy.logsAnalyzer.models.logic.output.BasicLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.models.util.Log;
import backend.academy.logsAnalyzer.services.filter.Filter;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.LogReflection;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogAnalyzerTest {
    private Filter filter;

    private List<Log> logs;
    private DateTimeFormatter dateFormat;

    @BeforeEach
    void setUp() throws ParseException {
        filter = new Filter(new LogReflection(), null, null, null, null);

        logs = new ArrayList<>();
        dateFormat = DateTimeFormatter.ISO_LOCAL_DATE;
        Log log0 =
            new Log("-", "-",
                get("2024-12-12"), "GET"
                , "-", "-", (short) 404, 123L, "-"
                , "MOZILLA/5.0 (WINDOWS NT 5.1) APPLEWEBKIT/537.36 (KHTML, LIKE GECKO) "
                + "CHROME/38.0.2125.104 SAFARI/537.36");
        Log log1 = new Log("-", "-",
            get("2024-12-13"), "GET"
            , "-", "-", (short) 201, 123L, "-"
            , "MOZILLA/5.0 (WINDOWS NT 5.1) APPLEWEBKIT/537.36 (KHTML, LIKE GECKO) "
            + "CHROME/38.0.2125.104 SAFARI/537.36");
        Log log2 = new Log("-", "-",
            get("2024-12-14"), "GET"
            , "-", "-", (short) 200, 123L, "-"
            , "MOZILLA/5.0 (WINDOWS NT 5.1) APPLEWEBKIT/537.36 (KHTML, LIKE GECKO) "
            + "CHROME/38.0.2125.104 SAFARI/537.36");
        Log log3 = new Log("-", "-",
            get("2024-12-14"), "GET"
            , "-", "-", (short) 300, 123L, "-"
            , "DEBIAN APT-HTTP/1.3 (0.8.10.3)");

        Log log4 = new Log("-", "-",
            get("2024-12-14"), "GET"
            , "-", "-", (short) 418, 123L, "-"
            , "WGET/1.15 (LINUX-GNU)");
        Log log5 = new Log("-", "-", get("2024-12-15")
            , "GET"
            , "-", "-", (short) 404, 123L, "-"
            , "MOZILLA/5.0 (WINDOWS NT 5.1) APPLEWEBKIT/537.36 (KHTML, LIKE GECKO) "
            + "CHROME/38.0.2125.104 SAFARI/537.36");

        logs.addAll(List.of(log0, log1, log2, log3, log4, log5));
    }

    private OffsetDateTime get(String text) {
        return LocalDate.parse(text, dateFormat).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
    }

    @Test
    void analyzer() {
        //Arrange
        String expected =
            "BasicLogsAnalyzerAnswer[resourcesUsed={-=6}, answerCodes={418=1, 404=2, 200=1, 201=1, 300=1}, responseCount=6, averageResponseTime=123, percentilel95ResponseSize=123, percentile5ResponseSize=108, median=102, averageDownTime=77760]";
        //Act
        LogAnalyzer logAnalyzer = new LogAnalyzer(filter);

        logs.forEach(logAnalyzer::add);
        BasicLogsAnalyzerAnswer analyze = logAnalyzer.analyze();
        //Assert
        Assertions.assertEquals(expected, analyze.toString().replaceAll("\r", ""));
    }
}

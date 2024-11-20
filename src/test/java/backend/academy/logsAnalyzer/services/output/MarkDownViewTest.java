package backend.academy.logsAnalyzer.services.output;

import backend.academy.logsAnalyzer.models.logic.output.ViewLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.models.util.Log;
import backend.academy.logsAnalyzer.services.analyzer.LogAnalyzer;
import backend.academy.logsAnalyzer.services.converter.ToStringConvertor;
import backend.academy.logsAnalyzer.services.filter.Filter;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.LogReflection;
import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MarkDownViewTest {
    private List<Log> logs;
    private DateTimeFormatter dateFormat;
    private Filter filter;

    @BeforeEach
    void setUp() throws ParseException {
        logs = new ArrayList<>();
        filter = new Filter(new LogReflection(), null, null, null, null);

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
        String expected = "### General information\n" +
            "\n" +
            "|               Metrics|      Value|\n" +
            "|:--------------------:|----------:|\n" +
            "|                 Files| 'logs.txt'|\n" +
            "|                  From|          -|\n" +
            "|                    To|          -|\n" +
            "| Average response size|        123|\n" +
            "|     95p response size|        123|\n" +
            "|      5p response size|        108|\n" +
            "|  median response size|        102|\n" +
            "|median server downtime|03:01:17.76|\n" +
            "\n" +
            "### Requested resources\n" +
            "\n" +
            "|Resource|Count|\n" +
            "|:------:|----:|\n" +
            "|       -|    6|\n" +
            "\n" +
            "### Response statuses\n" +
            "\n" +
            "|Code|          Status|Count|\n" +
            "|:--:|:--------------:|----:|\n" +
            "| 404|       Not Found|    2|\n" +
            "| 300|Multiple Choices|    1|\n" +
            "| 201|         Created|    1|\n" +
            "| 200|              OK|    1|\n" +
            "| 418|         Unknown|    1|\n" +
            "\n";
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        LogAnalyzer logAnalyzer = new LogAnalyzer(filter);
        logs.forEach(logAnalyzer::add);

        ViewLogsAnalyzerAnswer
            analyzerAnswer =
            new ViewLogsAnalyzerAnswer(logAnalyzer.analyze(), List.of("logs.txt"), filter.getFrom(), filter.getTo());
        //Act
        MarkDownView markDownView = new MarkDownView(new ToStringConvertor(), new PrintWriter(byteArrayOutputStream));

        markDownView.print(analyzerAnswer);

        //Assert
        Assertions.assertEquals(expected, byteArrayOutputStream.toString().replaceAll("\r", ""));
    }
}

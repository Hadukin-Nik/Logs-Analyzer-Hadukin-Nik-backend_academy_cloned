package backend.academy.logsAnalyzer.services.filter;

import backend.academy.logsAnalyzer.models.util.Log;
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
class FilterTest {
    private List<Log> logs;
    private DateTimeFormatter dateFormat;

    @BeforeEach
    void setUp() throws ParseException {
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
    void filter_certain_date_true() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), get("2024-12-12")
            , get("2024-12-12"), null, null);

        //Assert
        Assertions.assertTrue(filter.filter(logs.get(0)));
    }

    @Test
    void filter_certain_date_false() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), get("2024-12-12")
            , get("2024-12-12"), null, null);

        //Assert
        Assertions.assertFalse(filter.filter(logs.get(2)));
    }

    @Test
    void filter_from_date_true() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), get("2024-12-13")
            , null, null, null);

        //Assert
        Assertions.assertTrue(filter.filter(logs.get(1)));
    }

    @Test
    void filter_from_date_false() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), get("2024-12-13")
            , null, null, null);

        //Assert
        Assertions.assertFalse(filter.filter(logs.get(0)));
    }

    @Test
    void filter_before_date_true() throws ParseException {
        //Arrange
        //Act
        Filter filter =
            new Filter(new LogReflection(), null, get("2024-12-14")
                , null, null);

        //Assert
        Assertions.assertTrue(filter.filter(logs.get(2)));
    }

    @Test
    void filter_before_date_false() throws ParseException {
        //Arrange
        //Act
        Filter filter =
            new Filter(new LogReflection(), null, get("2024-12-14")
                , null, null);

        //Assert
        Assertions.assertFalse(filter.filter(logs.get(5)));
    }

    @Test
    void filter_by_user_true() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), null, null, "useragent", "mozilla/*");

        //Assert
        Assertions.assertTrue(filter.filter(logs.get(5)));
    }

    @Test
    void filter_by_user_alternative_true() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), null, null, "useragent", "*chrome*");

        //Assert
        Assertions.assertTrue(filter.filter(logs.get(5)));
    }

    @Test
    void filter_by_user_false() throws ParseException {
        //Arrange
        //Act
        Filter filter = new Filter(new LogReflection(), null, null, "useragent", "mozilla/*");

        //Assert
        Assertions.assertFalse(filter.filter(logs.get(3)));
    }
}

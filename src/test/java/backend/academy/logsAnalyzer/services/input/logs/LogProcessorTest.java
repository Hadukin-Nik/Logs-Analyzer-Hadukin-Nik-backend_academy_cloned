package backend.academy.logsAnalyzer.services.input.logs;

import backend.academy.logsAnalyzer.services.analyzer.ILogAnalyzer;
import backend.academy.logsAnalyzer.services.converter.LogConverter;
import backend.academy.logsAnalyzer.services.input.user.file.FileFinder;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogProcessorTest {
    private Logger logger;
    private ILogAnalyzer iLogAnalyzer;

    @BeforeEach
    void setUp() {
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Moscow"));

        logger = mock(Logger.class);

        doNothing().when(logger).error(anyString());
        doNothing().when(logger).warn(anyString());

        iLogAnalyzer = mock(ILogAnalyzer.class);
    }

    @Test
    void get_logs_from_url() {
        //Arrange
        String expected =
            "[link]";

        //Act
        LogProcessor fileNginxInputService =
            new LogProcessor(new LogConverter(), logger, new FileFinder(logger));
        String answer = "";
        List<String> logs = fileNginxInputService.addLogsToAnalyzer(iLogAnalyzer,
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            3);
        answer = logs.toString();
        //Assert
        String check = answer.replaceAll("\r", "").replaceAll("\n", "");
        Assertions.assertEquals(expected, check);
    }

    @Test
    void get_logs_from_resources() throws URISyntaxException {
        //Arrange
        String start = Paths.get(FileFinder.class.getClassLoader().getResource("logs").toURI()).toString();

        Set<String> expected = Set.of("logs.txt", "test1.log", "test2.log");

        //Act
        LogProcessor fileNginxInputService =
            new LogProcessor(new LogConverter(), logger, new FileFinder(logger));
        String answer = "";
        List<String> logs = fileNginxInputService.addLogsToAnalyzer(iLogAnalyzer, start + "/**",
            -1);

        //Assert
        Set<String> check = new HashSet<>(logs);

        Assertions.assertEquals(expected, check);
    }

}

package backend.academy.logsAnalyzer.services.input.user;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.models.logic.input.UserPreparedRequest;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.LogReflection;
import backend.academy.logsAnalyzer.services.output.ADOCView;
import backend.academy.logsAnalyzer.services.output.ILogicView;
import backend.academy.logsAnalyzer.services.output.MarkDownView;
import com.beust.jcommander.JCommander;
import java.net.URISyntaxException;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import org.assertj.core.util.Arrays;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class LogicInputTest {
    @Test
    void happy_path_url_with_filter() {
        //Arrange
        Map<String, ILogicView> views =
            Map.of("markdown", new MarkDownView(null, null), "adoc",
                new ADOCView(null, null));
        UserCommand args = new UserCommand();
        String[] argv = Arrays.array(
            "--path",
            "https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs",
            "--to", "2015-05-17", "--format", "adoc");
        JCommander.newBuilder()
            .addObject(args)
            .build()
            .parse(argv);

        //Act
        LogicInput logicInput =
            new LogicInput(views, new LogReflection(), views.get("markdown"), DateTimeFormatter.ISO_DATE);

        UserPreparedRequest userPreparedRequest = logicInput.answerOnRequest(
            args
        );

        //Assert
        Boolean ans = userPreparedRequest.chosenView() == views.get("adoc")
            && userPreparedRequest.filter().getTo().toString().equals("2015-05-17T00:00+03:00")
            && userPreparedRequest.filter().getFrom() == null;
        Assertions.assertTrue(ans);
    }

    @Test
    void happy_path_filepath_with_filter() throws URISyntaxException {
        //Arrange
        Map<String, ILogicView> views =
            Map.of("markdown", new MarkDownView(null, null), "adoc",
                new ADOCView(null, null));

        UserCommand args = new UserCommand();
        String[] argv = Arrays.array(
            "--path", "**/logs/test1/**.log", "--to", "2015-05-17",
            "--format", "adoc");
        JCommander.newBuilder()
            .addObject(args)
            .build()
            .parse(argv);

        //Act
        LogicInput logicInput =
            new LogicInput(views, new LogReflection(), views.get("markdown"), DateTimeFormatter.ISO_DATE);
        UserPreparedRequest userPreparedRequest = logicInput.answerOnRequest(args);

        //Assert
        Boolean ans = userPreparedRequest.chosenView() == views.get("adoc")
            && userPreparedRequest.filter().getTo().toString().equals("2015-05-17T00:00+03:00")
            && userPreparedRequest.filter().getFrom() == null;
        Assertions.assertTrue(ans);
    }
}

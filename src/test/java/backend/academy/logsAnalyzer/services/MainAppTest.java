package backend.academy.logsAnalyzer.services;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.services.initializer.IViewsInit;
import backend.academy.logsAnalyzer.services.initializer.ViewsInitImpl;
import backend.academy.logsAnalyzer.services.input.user.file.FileFinder;
import com.beust.jcommander.JCommander;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MainAppTest {
    @Test
    void happy_path() throws URISyntaxException {
        //Arrange
        String containingPart =
            "|                  From|                 2015-01-17T00:00:00|\n" +
                "|                    To|                                   -|\n" +
                "| Average response size|                             752_267|\n" +
                "|     95p response size|                          25_754_047|\n" +
                "|      5p response size|                                    |\n" +
                "|  median response size|                                 314|\n" +
                "|median server downtime|                            03:00:00|\n" +
                "\n" +
                "### Requested resources\n" +
                "\n" +
                "|            Resource|Count|\n" +
                "|:------------------:|----:|\n" +
                "|/downloads/product_1|   18|\n" +
                "|/downloads/product_2|   17|\n" +
                "\n" +
                "### Response statuses\n" +
                "\n" +
                "|Code|      Status|Count|\n" +
                "|:--:|:----------:|----:|\n" +
                "| 304|Not Modified|   17|\n" +
                "| 404|   Not Found|   12|\n" +
                "| 200|          OK|    6|\n";

        String start = Paths.get(FileFinder.class.getClassLoader().getResource("logs").toURI()) + "/**";

        String[] args = ("--path " + start + " --from 2015-01-17 --format markdown").split(" ");

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Logger log = mock(Logger.class);

        IViewsInit views = new ViewsInitImpl(new PrintStream(baos));
        UserCommand userCommand = new UserCommand();

        JCommander.newBuilder()
            .addObject(userCommand)
            .build()
            .parse(args);

        //Act
        MainApp mainApp =
            new MainApp(new PrintStream(baos), views, log);

        mainApp.run(userCommand);

        //Assert
        String replaced = baos.toString().replaceAll("\r", "");
        Assertions.assertTrue(
            replaced.contains(containingPart) && replaced.contains("logs.txt") && replaced.contains("test1.log")
                && replaced.contains("test2.log"));
    }

}

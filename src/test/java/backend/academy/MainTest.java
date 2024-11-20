package backend.academy;

import backend.academy.logsAnalyzer.services.input.user.file.FileFinder;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Test;

@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class MainTest {

    @Test
    void happy_path() throws URISyntaxException {
        //Arrange
        String start = Paths.get(FileFinder.class.getClassLoader().getResource("logs").toURI()) + "/**";

        String[] args = ("--path " + start + " --from 2015-01-17 --format markdown").split(" ");
        //Act
        Main.main(args);

    }
}

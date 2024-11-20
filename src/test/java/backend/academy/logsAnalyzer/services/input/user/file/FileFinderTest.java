package backend.academy.logsAnalyzer.services.input.user.file;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.List;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileFinderTest {

    @Test
    public void test() throws IOException, URISyntaxException {
        //Arrange
        String start = Paths.get(FileFinder.class.getClassLoader().getResource("logs").toURI()).toString();

        //Act
        FileFinder fileFinder = new FileFinder(null);
        List<File> filesByPattern = fileFinder.getFilesByPattern(start, "**/logs/test1/**.log");

        //Assert
        assertEquals(2, filesByPattern.size());
    }

}

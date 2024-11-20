package backend.academy.logsAnalyzer.services.input.user.file;

import java.io.File;
import java.util.List;

public interface IFileVisitor {
    List<File> getFilesByPattern(String startingDir, String pattern);
}

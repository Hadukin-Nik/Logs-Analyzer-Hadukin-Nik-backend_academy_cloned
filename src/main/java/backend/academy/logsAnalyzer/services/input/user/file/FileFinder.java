package backend.academy.logsAnalyzer.services.input.user.file;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import org.apache.logging.log4j.Logger;
import static java.nio.file.FileVisitResult.CONTINUE;

public class FileFinder implements IFileVisitor {
    private final Logger logger;

    public FileFinder(Logger logger) {
        this.logger = logger;
    }

    public List<File> getFilesByPattern(String startingDir, String pattern) {
        Path start = Paths.get(startingDir);

        String checkedPattern;

        if (!pattern.startsWith("**") && !pattern.startsWith(System.getProperty("user.dir"))) {
            checkedPattern = "**" + pattern;
        } else {
            checkedPattern = String.copyValueOf(pattern.toCharArray());
        }

        checkedPattern = checkedPattern.replaceAll("\\\\", "/");

        Finder finder = new Finder(checkedPattern);

        try {
            Files.walkFileTree(start, finder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return finder.foundFiles();
    }

    private class Finder extends SimpleFileVisitor<Path> {
        private final PathMatcher matcher;

        @Getter
        private List<File> foundFiles;

        Finder(String pattern) {
            matcher = FileSystems.getDefault()
                .getPathMatcher("glob:" + pattern);

            foundFiles = new ArrayList<>();
        }

        @Override
        public FileVisitResult visitFile(
            Path file,
            BasicFileAttributes attrs
        ) {
            if (matcher.matches(file)) {
                foundFiles.add(file.toFile());
            }
            return CONTINUE;
        }

        @Override
        public FileVisitResult visitFileFailed(
            Path file,
            IOException exc
        ) {
            logger.error(exc);
            return CONTINUE;
        }
    }
}

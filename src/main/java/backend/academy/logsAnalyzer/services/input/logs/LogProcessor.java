package backend.academy.logsAnalyzer.services.input.logs;

import backend.academy.logsAnalyzer.services.analyzer.ILogAnalyzer;
import backend.academy.logsAnalyzer.services.converter.ILogFromStringConverter;
import backend.academy.logsAnalyzer.services.input.user.file.IFileVisitor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.Logger;

@RequiredArgsConstructor
public class LogProcessor implements ILogProcessor {
    private final ILogFromStringConverter logConverter;
    private final Logger logger;
    private final IFileVisitor fileVisitor;

    @Override
    public List<String> addLogsToAnalyzer(ILogAnalyzer iLogAnalyzer, String path, int limit) {
        if (path.startsWith("http://") || path.startsWith("https://")) {
            try {
                addLogs(iLogAnalyzer, new InputStreamReader(URI.create(path).toURL().openStream()),
                    -1);

                return List.of("link");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            int lastSeparator = path.lastIndexOf(File.separator);
            String start = (lastSeparator == -1) ? "." : path.substring(0, lastSeparator);
            List<File> filesByPattern = fileVisitor.getFilesByPattern(start, path);
            List<String> fileNames = new ArrayList<>();

            for (File file : filesByPattern) {
                try {
                    addLogs(iLogAnalyzer, new FileReader(file), -1);
                    fileNames.add(file.getName());
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }

            return fileNames;
        }
    }

    private void addLogs(ILogAnalyzer iLogAnalyzer, InputStreamReader input, int limit) {
        try (BufferedReader reader = new BufferedReader(input)) {
            int errorCount = 0;
            int lineCount = 0;
            String line;

            while ((line = reader.readLine()) != null) {
                lineCount++;

                try {
                    iLogAnalyzer.add(logConverter.convert(line));
                } catch (RuntimeException e) {
                    errorCount++;
                }

                if (limit != -1 && lineCount - errorCount >= limit) {
                    break;
                }
            }

            if (errorCount > 0) {
                logger.warn("There were " + errorCount + " errors from " + lineCount + " lines.");
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}

package backend.academy.logsAnalyzer.services;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.models.logic.input.UserPreparedRequest;
import backend.academy.logsAnalyzer.models.logic.output.ViewLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.services.analyzer.ILogAnalyzer;
import backend.academy.logsAnalyzer.services.analyzer.LogAnalyzer;
import backend.academy.logsAnalyzer.services.converter.LogConverter;
import backend.academy.logsAnalyzer.services.initializer.IViewsInit;
import backend.academy.logsAnalyzer.services.input.logs.ILogProcessor;
import backend.academy.logsAnalyzer.services.input.logs.LogProcessor;
import backend.academy.logsAnalyzer.services.input.user.ILogicInput;
import backend.academy.logsAnalyzer.services.input.user.LogicInput;
import backend.academy.logsAnalyzer.services.input.user.file.FileFinder;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.LogReflection;
import backend.academy.logsAnalyzer.services.output.ILogicView;
import com.beust.jcommander.JCommander;
import java.io.PrintStream;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.logging.log4j.Logger;

public class MainApp {
    private final Logger logger;

    private final ILogicInput logicInput;

    public MainApp(
        PrintStream printStream,
        IViewsInit viewsInit,
        Logger logger
    ) {
        this.logger = logger;

        LogReflection fieldAccessor = new LogReflection();
        logicInput = new LogicInput(viewsInit.getViews(), fieldAccessor,
            viewsInit.getDefaultView(),
            DateTimeFormatter.ISO_DATE);

        printStream.print("Date pattern: ");
        printStream.println(DateTimeFormatter.ISO_DATE);
        printStream.println();

        printStream.print("Usable views: ");
        printStream.println(viewsInit.getViews().keySet().stream().collect(Collectors.joining(", ")));
        printStream.println();

        printStream.print("Usable parameters: ");
        UserCommand userCommand = new UserCommand();
        JCommander jCommander = new JCommander(userCommand);
        StringBuilder stringBuilder = new StringBuilder();
        jCommander.usage(stringBuilder);
        printStream.println(stringBuilder);
        printStream.println();

        printStream.print("Usable field filters (you can use any case you want): ");
        printStream.println(fieldAccessor.getFieldNames().stream().collect(Collectors.joining(", ")));
        printStream.println();

        printStream.println("Code examples");
        printStream.println("--path logs/**/test2.log");
        printStream.println(
            "--path https://raw.githubusercontent.com/elastic/examples/master/Common%20Data%20Formats/nginx_logs/nginx_logs --format adoc");
        printStream.println("--path logs/test1** --from 2015-01-17 --format markdown");
        printStream.println("--path logs/test1** --filter-field userAgent --filter-value Mozilla* --format adoc");
        printStream.println("--path logs/** --filter-field httpMethod --filter-value GET");
        printStream.println();

        printStream.println("if you want program to stop just print -1");
    }

    public void run(UserCommand userCommand) {
        try {
            UserPreparedRequest userPreparedRequest = logicInput.answerOnRequest(userCommand);

            ILogAnalyzer logAnalyzer = new LogAnalyzer(userPreparedRequest.filter());

            ILogProcessor nginxInputService =
                new LogProcessor(new LogConverter(), logger, new FileFinder(logger));

            List<String> filesUsed = nginxInputService.addLogsToAnalyzer(logAnalyzer, userPreparedRequest.path(), -1);

            ViewLogsAnalyzerAnswer forViewPreparedAnswer = new ViewLogsAnalyzerAnswer(
                logAnalyzer.analyze(), filesUsed, userPreparedRequest.filter().getFrom(),
                userPreparedRequest.filter().getTo());

            ILogicView chosenView = userPreparedRequest.chosenView();

            chosenView.print(forViewPreparedAnswer);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
        }
    }
}

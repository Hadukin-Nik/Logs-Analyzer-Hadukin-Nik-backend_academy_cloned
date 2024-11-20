package backend.academy;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.services.MainApp;
import backend.academy.logsAnalyzer.services.initializer.IViewsInit;
import backend.academy.logsAnalyzer.services.initializer.ViewsInitImpl;
import com.beust.jcommander.JCommander;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

@UtilityClass
@Log4j2
public class Main {
    public static void main(String[] args) {
        IViewsInit views = new ViewsInitImpl(System.out);
        UserCommand userCommand = new UserCommand();

        JCommander.newBuilder()
            .addObject(userCommand)
            .build()
            .parse(args);

        MainApp mainApp =
            new MainApp(System.out, views, log);

        mainApp.run(userCommand);
    }
}

package backend.academy.logsAnalyzer.services.initializer;

import backend.academy.logsAnalyzer.services.converter.IToString;
import backend.academy.logsAnalyzer.services.converter.ToStringConvertor;
import backend.academy.logsAnalyzer.services.output.ADOCView;
import backend.academy.logsAnalyzer.services.output.ILogicView;
import backend.academy.logsAnalyzer.services.output.MarkDownView;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.Map;

public class ViewsInitImpl implements IViewsInit {
    private final Map<String, ILogicView> views;
    private final ILogicView defaultView;

    @SuppressWarnings("MultipleStringLiterals")
    public ViewsInitImpl(OutputStream outputStream) {
        IToString toString = new ToStringConvertor();

        views =
            Map.of("markdown", new MarkDownView(toString, new PrintWriter(new OutputStreamWriter(outputStream))),
                "adoc",
                new ADOCView(toString, new PrintWriter(new OutputStreamWriter(outputStream))));

        defaultView = views.get("markdown");
    }

    @Override
    public Map<String, ILogicView> getViews() {
        return views;
    }

    @Override
    public ILogicView getDefaultView() {
        return defaultView;
    }
}

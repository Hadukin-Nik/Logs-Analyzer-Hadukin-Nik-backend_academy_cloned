package backend.academy.logsAnalyzer.services.initializer;

import backend.academy.logsAnalyzer.services.output.ILogicView;
import java.util.Map;

public interface IViewsInit {
    Map<String, ILogicView> getViews();

    ILogicView getDefaultView();
}

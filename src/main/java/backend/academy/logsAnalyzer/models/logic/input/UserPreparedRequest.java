package backend.academy.logsAnalyzer.models.logic.input;

import backend.academy.logsAnalyzer.services.filter.IFilter;
import backend.academy.logsAnalyzer.services.output.ILogicView;

public record UserPreparedRequest(ILogicView chosenView, IFilter filter, String path) {
}

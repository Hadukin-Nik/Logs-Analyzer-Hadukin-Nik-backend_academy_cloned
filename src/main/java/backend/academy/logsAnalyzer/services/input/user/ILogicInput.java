package backend.academy.logsAnalyzer.services.input.user;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.models.logic.input.UserPreparedRequest;

public interface ILogicInput {
    UserPreparedRequest answerOnRequest(UserCommand userCommand);
}

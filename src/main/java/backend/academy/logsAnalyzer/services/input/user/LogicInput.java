package backend.academy.logsAnalyzer.services.input.user;

import backend.academy.logsAnalyzer.models.basic.UserCommand;
import backend.academy.logsAnalyzer.models.logic.input.UserPreparedRequest;
import backend.academy.logsAnalyzer.services.filter.Filter;
import backend.academy.logsAnalyzer.services.filter.IFilter;
import backend.academy.logsAnalyzer.services.logFieldsAccessor.ILogFieldAccessor;
import backend.academy.logsAnalyzer.services.output.ILogicView;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LogicInput implements ILogicInput {
    private final Map<String, ILogicView> usableViews;
    private final ILogFieldAccessor fieldAccessor;
    private final ILogicView defaultView;
    private final DateTimeFormatter datePattern;

    @Override
    public UserPreparedRequest answerOnRequest(UserCommand userCommand) {
        return new UserPreparedRequest(
            getViewFromUsersRequest(userCommand.viewFormat()),
            getFilterFromUsersRequest(userCommand, datePattern),
            getLogsFromUsersRequest(userCommand.path())
        );
    }

    private ILogicView getViewFromUsersRequest(String value) {
        if (value == null) {
            return defaultView;
        }

        String lowerCaseValue = value.toLowerCase();

        if (usableViews.containsKey(lowerCaseValue)) {
            return usableViews.get(lowerCaseValue);
        } else {
            throw new IllegalArgumentException("Invalid view name: " + lowerCaseValue);
        }
    }

    private String getLogsFromUsersRequest(String value) {
        if (value == null) {
            throw new IllegalArgumentException("You must provide a user path request");
        }

        return value;
    }

    @SuppressWarnings("MultipleStringLiterals")
    private IFilter getFilterFromUsersRequest(UserCommand values, DateTimeFormatter dateFormat) {
        String rawDateFrom = values.dateFrom();
        String rawDateTo = values.dateTo();
        String filterField = values.filterField();
        String filterValue = values.filterValue();

        OffsetDateTime dateFrom = null;
        OffsetDateTime dateTo = null;

        if (rawDateFrom != null) {
            try {
                dateFrom = get(rawDateFrom, dateFormat);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Wrong time type: " + rawDateFrom);
            }
        }

        if (rawDateTo != null) {
            try {
                dateTo = get(rawDateTo, dateFormat);
            } catch (RuntimeException e) {
                throw new IllegalArgumentException("Wrong time type: " + rawDateTo);
            }
        }

        return new Filter(fieldAccessor, dateFrom, dateTo, filterField, filterValue);
    }

    private OffsetDateTime get(String text, DateTimeFormatter dateFormat) {
        return LocalDate.parse(text, dateFormat).atStartOfDay(ZoneId.systemDefault()).toOffsetDateTime();
    }
}

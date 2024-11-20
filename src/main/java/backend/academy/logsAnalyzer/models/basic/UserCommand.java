package backend.academy.logsAnalyzer.models.basic;

import com.beust.jcommander.Parameter;
import lombok.Getter;

@Getter
public class UserCommand {
    @Parameter(names = "--path")
    private String path;

    @Parameter(names = "--from")
    private String dateFrom;

    @Parameter(names = "--to")
    private String dateTo;

    @Parameter(names = "--format")
    private String viewFormat;

    @Parameter(names = "--filter-field")
    private String filterField;

    @Parameter(names = "--filter-value")
    private String filterValue;
}

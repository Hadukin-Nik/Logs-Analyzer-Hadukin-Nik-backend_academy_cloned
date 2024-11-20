package backend.academy.logsAnalyzer.services.output;

import backend.academy.logsAnalyzer.models.logic.output.BasicLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.models.logic.output.ViewLogsAnalyzerAnswer;
import backend.academy.logsAnalyzer.services.converter.IToString;
import java.io.Writer;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import org.apache.commons.httpclient.HttpStatus;

public abstract class BasicLogicView implements ILogicView {
    protected final IToString converter;
    protected final Writer output;

    protected BasicLogicView(IToString converter, Writer out) {
        this.converter = converter;
        this.output = out;
    }

    protected abstract void drawTables(
        List<List<List<String>>> tablesContent,
        List<List<String>> tableHeaders,
        List<String> titles
    );

    @SuppressWarnings("MultipleStringLiterals")
    @Override
    public void print(ViewLogsAnalyzerAnswer report) {
        BasicLogsAnalyzerAnswer numbers = report.basicAnswer();

        List<String> titles = Arrays.asList("General information", "Requested resources", "Response statuses");

        List<List<String>> tableHeaders = Arrays.asList(
            Arrays.asList("Metrics", "Value"),
            Arrays.asList("Resource", "Count"),
            Arrays.asList("Code", "Status", "Count")
        );

        List<List<List<String>>> tables = new ArrayList<>();

        List<List<String>> generalInformation = new ArrayList<>(
            Arrays.asList(
                Arrays.asList("Files", converter.convert(report.files())),
                Arrays.asList("From", converter.convert(report.from())),
                Arrays.asList("To", converter.convert(report.to())),
                Arrays.asList("Average response size", converter.convert(numbers.averageResponseTime())),
                Arrays.asList("95p response size", converter.convert(numbers.percentilel95ResponseSize())),
                Arrays.asList("5p response size", converter.convert(numbers.percentile5ResponseSize())),
                Arrays.asList("median response size", converter.convert(numbers.median())),
                Arrays.asList("median server downtime",
                    converter.convert(Instant.ofEpochMilli(numbers.averageDownTime())))
            )
        );

        List<List<String>> requestedResources = new ArrayList<>();
        //creating a list of used resources, sorting them by count from map, and then reversing it
        List<String> resourcesList =
            new ArrayList<>(numbers.resourcesUsed().keySet().stream().toList().stream().sorted(
                    Comparator.comparing(o -> numbers.resourcesUsed().get(o))).sorted(Comparator.reverseOrder())
                .toList());
        Collections.reverse(resourcesList);
        //creating a list for table
        for (var i : resourcesList) {
            requestedResources.add(
                Arrays.asList(i, converter.convert((long) numbers.resourcesUsed().get(i)))
            );
        }

        List<List<String>> responseStatuses = new ArrayList<>();
        //creating a list of status codes, sorting them by count from map, and then reversing it
        List<Short> statusesList =
            new ArrayList<>(numbers.answerCodes().keySet().stream().toList().stream().sorted(
                Comparator.comparing(o -> numbers.answerCodes().get(o))).toList());
        Collections.reverse(statusesList);
        //creating a list for table
        for (var i : statusesList) {
            responseStatuses.add(Arrays.asList(
                i.toString(),
                HttpStatus.getStatusText(i) == null ? "Unknown" : HttpStatus.getStatusText(i),
                converter.convert((long) numbers.answerCodes().get(i))
            ));
        }

        tables.add(generalInformation);
        tables.add(requestedResources);
        tables.add(responseStatuses);

        for (int i = 0; i < tables.size(); i++) {
            int rows = tableHeaders.get(i).size();

            for (var j : tables.get(i)) {
                if (rows != j.size()) {
                    throw new IllegalArgumentException("Tables do not have the same number of columns");
                }
            }
        }

        drawTables(tables, tableHeaders, titles);
    }
}

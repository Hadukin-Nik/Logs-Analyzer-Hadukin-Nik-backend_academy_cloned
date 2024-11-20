package backend.academy.logsAnalyzer.services.output;

import backend.academy.logsAnalyzer.services.converter.IToString;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class MarkDownView extends BasicLogicView {

    public MarkDownView(IToString converter, Writer output) {
        super(converter, output);
    }

    private String repeat(int count) {
        return new String(new char[count]).replace("\0", "-");
    }

    private String createTable(List<List<String>> rows, List<String> headers) {
        List<Integer> maxSize = new ArrayList<>();
        for (int j = 0; j < headers.size(); j++) {
            final int search = j;

            maxSize.add(Math.max(headers.get(j).length(),
                rows.stream().map(r -> r.get(search).length()).max(Integer::compare).orElse(0)
            ));
        }

        StringBuilder table = new StringBuilder();

        StringBuilder rowSchemeBuilder = new StringBuilder();
        rowSchemeBuilder.append("|");
        for (var max : maxSize) {
            rowSchemeBuilder.append("%").append(max).append("s|");
        }
        String tableRowScheme = rowSchemeBuilder.toString();
        table.append(String.format(tableRowScheme, headers.toArray())).append("\n");

        table.append("|");
        maxSize.stream().limit(maxSize.size() - 1)
            .forEach(s -> table.append(":").append(repeat(Math.max(0, s - 2))).append(":|"));
        table.append(repeat(Math.max(0, maxSize.getLast() - 1))).append(":|\n");

        rows.forEach(s -> table.append(String.format(tableRowScheme, s.toArray())).append("\n"));

        table.append("\n");
        return table.toString();
    }

    @Override
    protected void drawTables(
        List<List<List<String>>> tablesContent,
        List<List<String>> tableHeaders,
        List<String> titles
    ) {
        try {
            BufferedWriter bw = new BufferedWriter(output);
            for (int i = 0; i < titles.size(); i++) {
                String title = titles.get(i);
                List<String> headers = tableHeaders.get(i);
                List<List<String>> rows = tablesContent.get(i);

                bw.write("### " + title + "\n\n");
                bw.write(createTable(rows, headers));

                bw.flush();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}

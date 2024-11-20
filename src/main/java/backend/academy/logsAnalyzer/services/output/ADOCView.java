package backend.academy.logsAnalyzer.services.output;

import backend.academy.logsAnalyzer.services.converter.IToString;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.stream.Collectors;

public class ADOCView extends BasicLogicView {

    public ADOCView(IToString converter, Writer output) {
        super(converter, output);
    }

    private String createTable(List<String> table, List<String> header) {
        StringBuilder builder = new StringBuilder();

        builder.append("[cols=\"" + header.stream().map(s -> "1").collect(Collectors.joining(",")) + "\"]\n\n");

        builder.append("|===\n");

        builder.append("|" + header.stream().collect(Collectors.joining(" |")) + "\n\n");

        builder.append(table.stream().map(cell -> "|" + cell + "\n").collect(Collectors.joining()));

        builder.append("|===\n\n");

        return builder.toString();
    }

    @Override
    protected void drawTables(
        List<List<List<String>>> tablesContent,
        List<List<String>> tableHeaders,
        List<String> titles
    ) {
        try {
            BufferedWriter bufferedWriter = new BufferedWriter(output);

            for (int i = 0; i < tablesContent.size(); i++) {
                String title = titles.get(i);

                List<String> rows = tablesContent.get(i).stream().flatMap(List::stream).toList();
                List<String> headers = tableHeaders.get(i);

                bufferedWriter.append("= " + title + "\n");

                bufferedWriter.append(createTable(rows, headers));
            }

            bufferedWriter.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

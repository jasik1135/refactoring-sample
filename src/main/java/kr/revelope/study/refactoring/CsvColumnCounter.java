package kr.revelope.study.refactoring;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class CsvColumnCounter {
    private final int targetColumnIndex;
    private final int columnCount;

    public CsvColumnCounter(Stream<String> lines, String columnName) {
        String[] columns = lines.limit(1)
                .map(line -> line.split(","))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("First line must be columns. Column can not found."));

        this.columnCount = columns.length;

        this.targetColumnIndex = IntStream.range(0, this.columnCount)
                .filter(columnIndex -> columnName.equals(columns[columnIndex]))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Can not found target column '" + columnName + "'"));
    }

    public Map<String, Integer> getResult(Stream<String> lines) {
        return lines.skip(1)
                .map(line -> line.split(","))
                .filter(dataArray -> dataArray.length == columnCount)
                .map(dataArray -> dataArray[targetColumnIndex])
                .collect(Collectors.groupingBy(Function.identity(), Collectors.summingInt(value -> 1)));
    }
}

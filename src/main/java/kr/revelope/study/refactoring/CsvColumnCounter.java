package kr.revelope.study.refactoring;

import kr.revelope.study.refactoring.argument.ColumnOption;
import org.apache.commons.lang3.ObjectUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CsvColumnCounter {
    private final Map<Integer, Integer> targetColumnIndexToValue;
    private final int columnCount;

    public CsvColumnCounter(Stream<String> lines, List<ColumnOption> columnOptionList) {
        List<String> columnList = lines.limit(1)
                .map(line -> line.split(","))
                .map(Arrays::asList)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("First line must be columns. Column can not found."));

        this.columnCount = columnList.size();

        this.targetColumnIndexToValue = columnOptionList.stream()
                .collect(HashMap::new,
                        (map, columnOption) -> map.put(columnList.indexOf(columnOption.getColumn()), columnOption.getValue()),
                        HashMap::putAll);

        if (this.targetColumnIndexToValue.size() == 0) {
            throw new IllegalStateException(String.format("Can not find target columns '%s'", columnOptionList));
        }
    }

    public Map<List<String>, Integer> getResult(Stream<String> lines) {
        return lines.skip(1)
                .map(line -> line.split(","))
                .filter(dataArray -> dataArray.length == this.columnCount)
                .filter(dateArray -> targetColumnIndexToValue.entrySet().stream()
                        .filter(entry -> ObjectUtils.isNotEmpty(entry.getValue()))
                        .map(entry -> Integer.parseInt(dateArray[entry.getKey()]) > entry.getValue())
                        .reduce(true, (result, current) -> result && current))
                .map(dataArray -> targetColumnIndexToValue.keySet().stream()
                        .map(targetIndex -> dataArray[targetIndex])
                        .collect(Collectors.toList()))
                .collect(Collectors.groupingBy(dataList -> dataList, Collectors.summingInt(value -> 1)));
    }
}

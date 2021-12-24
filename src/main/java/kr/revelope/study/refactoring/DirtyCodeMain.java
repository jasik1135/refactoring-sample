package kr.revelope.study.refactoring;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;

/**
 * csv 파일을 읽어서 특정 컬럼명을 group by 하여 출력하는 프로그램이다.
 * args[0] : resources에 보관된 csv 파일명
 * args[1] : 카운트 할 컬럼명
 * <p>
 * 아래 코드를 리팩토링 해보시오
 */
public class DirtyCodeMain {
    private static final String BASE_PATH = "src/main/resources/";

    public static void main(String[] args) {
        if (args == null || args.length < 2) {
            throw new IllegalArgumentException("File name and target column name is required.");
        }

        CsvColumnCounter columnCounter = new CsvColumnCounter();

        processFile(args[0], lines -> columnCounter.setFields(lines, args[1]));

        Map<String, Integer> result = processFile(args[0], columnCounter::getResult);

        for (Map.Entry<String, Integer> entry : result.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }
    }

    private static <T> T processFile(String fileName, Function<Stream<String>, T> function) {
        try (Stream<String> lines = Files.lines(Paths.get(BASE_PATH + fileName), StandardCharsets.UTF_8)) {
            return function.apply(lines);
        } catch (IOException ex) {
            throw new IllegalArgumentException("'" + fileName + "' file can not found.");
        }
    }
}

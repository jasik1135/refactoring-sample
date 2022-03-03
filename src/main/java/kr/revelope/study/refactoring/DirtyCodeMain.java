package kr.revelope.study.refactoring;

import kr.revelope.study.refactoring.argument.ArgumentParser;
import kr.revelope.study.refactoring.argument.ColumnOption;
import kr.revelope.study.refactoring.argument.OptionType;

import java.util.List;
import java.util.Map;

/**
 * csv 파일을 읽어서 특정 컬럼명을 group by 하여 출력하는 프로그램이다.
 * args[0] : resources에 보관된 csv 파일명
 * args[1] : 카운트 할 컬럼명
 * <p>
 * 아래 코드를 리팩토링 해보시오
 */

/**
 * todo 3.
 * -c를 여러개 줄 수 있도록 해주세요. (순서중요)
 * <p>
 * todo 4.
 * -f file.csv -c column1 -v value1 -c column2 -v value2
 * column1이 숫자 칼럼인 경우 value1 값보다 크고 column2가 숫자 칼럼인 경우 value2 값보다 큰 데이터에 대하여 그룹 카운트
 * -f file.csv -c column1 -c column2 -v value
 * column2가 숫자 칼럼인 경우 value2 값보다 큰 데이터에 대하여 그룹 카운트
 */

public class DirtyCodeMain {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser(args);

        final String fileName = argumentParser.getOptionValue(OptionType.FILE_NAME);
        final List<ColumnOption> columnOptionList = argumentParser.getColumnOptionList();

        final FileProcessor fileProcessor = FileProcessor.getInstance();
        fileProcessor.setCharacterSetFromFile(fileName);

        CsvColumnCounter columnCounter = fileProcessor.processFile(fileName,
                lines -> new CsvColumnCounter(lines, columnOptionList)
        );

        Map<List<String>, Integer> result = fileProcessor.processFile(fileName, columnCounter::getResult);
        System.out.println("Count : " + result.size());
        result.forEach((key, value) ->
                System.out.println(key + " : " + value)
        );
    }
}

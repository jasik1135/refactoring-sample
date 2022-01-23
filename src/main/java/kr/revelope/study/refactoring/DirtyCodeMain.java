package kr.revelope.study.refactoring;

/**
 * csv 파일을 읽어서 특정 컬럼명을 group by 하여 출력하는 프로그램이다.
 * args[0] : resources에 보관된 csv 파일명
 * args[1] : 카운트 할 컬럼명
 * <p>
 * 아래 코드를 리팩토링 해보시오
 */

public class DirtyCodeMain {
    public static void main(String[] args) {
        ArgumentParser argumentParser = new ArgumentParser(args);

        final String fileName = argumentParser.getOptionValue("f");
        final String columnName = argumentParser.getOptionValue("c");

        final FileProcessor fileProcessor = FileProcessor.getInstance();
        fileProcessor.setCharacterSetFromFile(fileName);

        CsvColumnCounter columnCounter = fileProcessor.processFile(fileName,
                lines -> new CsvColumnCounter(lines, columnName)
        );

        fileProcessor.processFile(fileName, columnCounter::getResult)
                .forEach((key, value) ->
                        System.out.println(key + " : " + value)
                );
    }


}
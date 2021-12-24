package kr.revelope.study.refactoring;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * csv 파일을 읽어서 특정 컬럼명을 group by 하여 출력하는 프로그램이다.
 * args[0] : resources에 보관된 csv 파일명
 * args[1] : 카운트 할 컬럼명
 *
 * 아래 코드를 리팩토링 해보시오
 */
public class DirtyCodeMain {
	public static void main(String[] args) {
		if (args == null || args.length < 2) {
			throw new IllegalArgumentException("File name and target column name is required.");
		}

		int initialTargetColumnIndex;
		int initialColumnCount;
		try (Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", args[0]), StandardCharsets.UTF_8)) {
			String[] columns = lines.limit(1)
					.map(line -> line.split(","))
					.findFirst()
					.orElseThrow(() -> new IllegalArgumentException("First line must be columns. Column can not found."));

			initialColumnCount = columns.length;

			initialTargetColumnIndex = IntStream.range(0, initialColumnCount)
					.filter(columnIndex -> args[1].equals(columns[columnIndex]))
					.findFirst()
					.orElseThrow(() -> new IllegalStateException("Can not found target column '" + args[1] + "'"));

		} catch (IOException ex) {
			throw new IllegalArgumentException("'" + args[0] + "' file can not found.");
		}

		final int targetColumnIndex = initialTargetColumnIndex;
		final int columnCount = initialColumnCount;
		Map<String, Integer> result = new HashMap<>();

		try (Stream<String> lines = Files.lines(Paths.get("src", "main", "resources", args[0]), StandardCharsets.UTF_8)) {
			lines.skip(1)
				.map(line -> line.split(","))
				.filter(dataArray -> dataArray.length == columnCount)
				.map(dataArray -> dataArray[targetColumnIndex])
				.forEach(data -> result.put(data, result.getOrDefault(data, 1)));
		} catch (IOException ex) {
			throw new IllegalArgumentException("'" + args[0] + "' file can not found.");
		}

		for (Map.Entry<String, Integer> entry : result.entrySet()) {
			System.out.println(entry.getKey() + " : " + entry.getValue());
		}
	}
}

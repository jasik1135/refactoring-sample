package kr.revelope.study.refactoring;

import org.apache.any23.encoding.TikaEncodingDetector;
import org.apache.commons.io.FileUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.function.Function;
import java.util.stream.Stream;

public class FileProcessor {
    private static final TikaEncodingDetector ENCODING_DETECTOR = new TikaEncodingDetector();
    private static final String DEFAULT_ENCODING = "UTF-8";
    private static final String BASE_PATH = "src/main/resources/";

    private static final FileProcessor INSTANCE = new FileProcessor();

    private Charset characterSet = StandardCharsets.UTF_8;

    private FileProcessor() {
    }

    public static FileProcessor getInstance() {
        return INSTANCE;
    }

    public void setCharacterSetFromFile(String fileName) {
        try {
            this.characterSet = guessEncodingFromFile(fileName);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    private Charset guessEncodingFromFile(String fileName) throws IOException {
        InputStream inputStream = FileUtils.openInputStream(FileUtils.getFile(BASE_PATH + fileName));
        String encoding = ENCODING_DETECTOR.guessEncoding(inputStream);

        if (!Charset.isSupported(encoding)) {
            encoding = DEFAULT_ENCODING;
        }

        return Charset.forName(encoding);
    }

    public <T> T processFile(String fileName, Function<Stream<String>, T> function) {
        try (Stream<String> lines = Files.lines(Paths.get(BASE_PATH + fileName), this.characterSet)) {
            return function.apply(lines);
        } catch (IOException ex) {
            throw new IllegalArgumentException("'" + fileName + "' file can not found.");
        }
    }
}

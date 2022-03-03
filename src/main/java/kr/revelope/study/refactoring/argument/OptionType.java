package kr.revelope.study.refactoring.argument;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public enum OptionType {
    FILE_NAME("f"),
    COLUMN_NAME("c"),
    VALUE("v");

    static final Map<String, OptionType> STRING_TO_OPTION = Arrays.stream(values()).collect(Collectors.toMap(OptionType::getOptionString, option -> option));

    private final String optionString;

    OptionType(String optionString) {
        this.optionString = optionString;
    }

    static OptionType getOption(String OptionString) {
        return STRING_TO_OPTION.get(OptionString);
    }

    public String getOptionString() {
        return optionString;
    }
}

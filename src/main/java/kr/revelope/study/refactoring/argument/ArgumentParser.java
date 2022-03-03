package kr.revelope.study.refactoring.argument;

import org.apache.commons.cli.*;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ArgumentParser {
    private static final CommandLineParser DEFAULT_PARSER = new DefaultParser();
    private static final Option[] OPTION_ARRAY = {
            Option.builder("f")
                    .longOpt("file-path")
                    .hasArg(true)
                    .desc("file path")
                    .required()
                    .build(),
            Option.builder("c")
                    .longOpt("column-name")
                    .hasArg()
                    .desc("column name")
                    .required()
                    .build(),
            Option.builder("v")
                    .hasArg()
                    .desc("numeric column lower bound")
                    .build()
    };

    private final Options options = new Options();
    private final CommandLine commandLine;

    // todo
    public final List<ColumnOption> columnOptionList = new ArrayList<>();

    public ArgumentParser(String[] args) {
        initializeOptions();

        try {
            this.commandLine = parseArgument(args);
        } catch (ParseException exception) {
            throw new IllegalArgumentException(String.format("Argument parsing failed: %s", exception.getMessage()));
        }

        initializeColumnOptionList();
    }

    private void initializeOptions() {
        for (Option option : OPTION_ARRAY) {
            options.addOption(option);
        }
    }

    private void initializeColumnOptionList() {
        Option[] options = this.commandLine.getOptions();
        for (int i = 0; i < options.length; i++) {
            if (!StringUtils.equals(options[i].getOpt(), OptionType.COLUMN_NAME.getOptionString())) {
                continue;
            }

            Integer value = null;
            if (i != options.length - 1 && StringUtils.equals(options[i + 1].getOpt(), OptionType.VALUE.getOptionString())) {
                try {
                    value = Integer.parseInt(options[i + 1].getValue());
                } catch(NumberFormatException exception) {
                    value = null;
                }
            }

            this.columnOptionList.add(new ColumnOption(options[i].getValue(), value));
        }
    }

    private CommandLine parseArgument(String[] args) throws ParseException {
        return DEFAULT_PARSER.parse(options, args);
    }

    public String getOptionValue(OptionType option) {
        String optionValue = this.commandLine.getOptionValue(option.getOptionString());
        if (optionValue == null) {
            throw new IllegalArgumentException(String.format("Option '%s' does not exist", option.getOptionString()));
        }

        return optionValue;
    }

    public List<ColumnOption> getColumnOptionList() {
        return columnOptionList;
    }
}

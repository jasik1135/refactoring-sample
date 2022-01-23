package kr.revelope.study.refactoring;

import org.apache.commons.cli.*;

public class ArgumentParser {
    private static final CommandLineParser DEFAULT_PARSER = new DefaultParser();

    private static final Option[] OPTION_ARRAY = {
            Option.builder("f")
                    .longOpt("file-path")
                    .hasArg(true)
                    .desc("file path")
                    .build(),
            Option.builder("c")
                    .longOpt("column-name")
                    .hasArg(true)
                    .desc("column name")
                    .build()
    };

    private final Options options = new Options();

    private final CommandLine commandLine;

    public ArgumentParser(String[] args) {
        for (Option option : OPTION_ARRAY) {
            options.addOption(option);
        }

        try {
            commandLine = parseArgument(args);
        } catch (ParseException exception) {
            throw new IllegalArgumentException("Arguments do not match options.");
        }
    }

    private CommandLine parseArgument(String[] args) throws ParseException {
        return DEFAULT_PARSER.parse(options, args);
    }

    public String getOptionValue(String option) {
        String optionValue = commandLine.getOptionValue(option);
        if (optionValue == null) {
            throw new IllegalArgumentException(String.format("Option '%s' does not exist", option));
        }

        return optionValue;
    }
}

package kr.revelope.study.refactoring.argument;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@ToString
public class ColumnOption {
    private final String column;
    private final Integer value;
}

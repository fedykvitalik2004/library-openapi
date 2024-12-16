package org.vitalii.fedyk.librarygenerated.search;

import lombok.Getter;
import org.vitalii.fedyk.librarygenerated.constant.ExceptionConstants;

import java.util.Arrays;

@Getter
public enum SearchOperation {
    EQUALS(":"),
    NOT_EQUALS("!:"),
    GREATER_THAN(">"),
    LESS_THAN("<"),
    GREATER_THAN_EQUALS(">="),
    LESS_THAN_EQUALS("<="),
    CONTAINS("contains");

    private final String operation;

    SearchOperation(String operation) {
        this.operation = operation;
    }

    public static SearchOperation findByValue(final String operation) {
        return Arrays.stream(SearchOperation.values())
                .filter(o -> o.operation.equals(operation))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(ExceptionConstants.SEARCH_OPERATION_NOT_FOUND));
    }
}

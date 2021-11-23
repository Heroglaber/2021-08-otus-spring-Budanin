package ru.otus.library.shell.utils;

import org.springframework.shell.Input;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class InputWithCommandArguments implements Input {
    private final String input;

    public InputWithCommandArguments(String input) {
        this.input = input;
    }

    @Override
    public String rawText() {
        return input;
    }

    @Override
    public List<String> words() {
        List<String> splittedWords = "".equals(this.rawText()) ? Collections.emptyList() : Arrays.asList(this.rawText().split(" (?=(([^'\"]*['\"]){2})*[^'\"]*$)"));
        return splittedWords.stream().map(string -> string.replaceAll("^'|'$", "")).collect(Collectors.toList());
    }
}

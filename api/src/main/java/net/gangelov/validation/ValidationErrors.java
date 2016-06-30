package net.gangelov.validation;

import net.gangelov.utils.Strings;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class ValidationErrors {
    public Map<String, List<String>> errors = new TreeMap<>();

    public void addFrom(ValidationErrors other) {
        other.errors.forEach(this::add);
    }

    public void add(String key, String message) {
        if (!errors.containsKey(key)) {
            errors.put(key, new ArrayList<>());
        }

        errors.get(key).add(message);
    }

    public void add(String key, List<String> messages) {
        if (!errors.containsKey(key)) {
            errors.put(key, new ArrayList<>());
        }

        errors.get(key).addAll(messages);
    }

    public boolean hasAny() {
        return errors.size() > 0;
    }

    public boolean valid() {
        return !hasAny();
    }

    public boolean invalid() {
        return hasAny();
    }

    public List<String> toErrorStrings() {
        return errors.entrySet().stream()
                .map(entry -> Strings.toSentence(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return String.join(" ", toErrorStrings());
    }
}

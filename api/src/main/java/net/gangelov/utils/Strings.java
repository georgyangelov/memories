package net.gangelov.utils;

import java.util.List;

public class Strings {
    public static String toSentence(String subject, List<String> statementList) {
        StringBuilder str = new StringBuilder();

        str.append(capitalize(subject));

        int statementCount = statementList.size();

        str.append(" ");
        if (statementCount > 1) {
            str.append(String.join(", ", statementList.subList(0, statementCount - 1)));
            str.append(" and ");
            str.append(statementList.get(statementCount - 1));
        } else if (statementCount == 1) {
            str.append(statementList.get(0));
        }

        str.append(".");

        return str.toString();
    }

    public static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String squish(String str) {
        if (str == null) {
            return null;
        }

        return str.replaceAll("\\s+", " ").replaceAll("^\\s|\\s$", "");
    }

    public static boolean isPresent(String str) {
        String s = Strings.squish(str);

        return s != null && !s.isEmpty();
    }

    public static boolean isBlank(String str) {
        return !isPresent(str);
    }

    public static String getExtension(String path) {
        return path.substring(
                path.lastIndexOf(".") + 1,
                path.length()
        );
    }
}

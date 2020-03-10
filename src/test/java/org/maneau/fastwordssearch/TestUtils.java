package org.maneau.fastwordssearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TestUtils {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);

    public static String loadTextFile(String fileName) {
        StringBuilder sbText = new StringBuilder();

        InputStream inputStream = LOG.getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            LOG.error("File not found " + fileName);
            return null;
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                sbText.append(scanner.nextLine()).append('\n');
            }
        }
        return sbText.toString();
    }

    public static List<String> loadFirstNameFile() {
        String fileName = "/firstname.lst";
        List<String> keywords = new ArrayList<String>();

        InputStream inputStream = LOG.getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            LOG.error("File not found " + fileName);
            return null;
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                keywords.add(scanner.nextLine());
            }
        }
        return keywords;
    }


    public static String[] toStringArray(List<MatchToken> matchTokens) {
        String[] arrayStr = new String[matchTokens.size()];
        for (int i = 0; i < matchTokens.size(); i++) {
            arrayStr[i] = matchTokens.get(i).getKeyword();
        }
        return arrayStr;
    }

    public static String toString(List<MatchToken> matchTokens) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < matchTokens.size(); i++) {
            sb.append(matchTokens.get(i).getKeyword()).append(", ");
        }
        return sb.toString();
    }
}

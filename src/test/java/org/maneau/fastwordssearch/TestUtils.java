package org.maneau.fastwordssearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

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
        List<String> keywords = new ArrayList<>();

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
        for (MatchToken matchToken : matchTokens) {
            sb.append(matchToken.getKeyword()).append(", ");
        }
        return sb.toString();
    }

    public static void assertTokenEquals(MatchToken expectedToken, MatchToken token) {
        if (!expectedToken.equals(token)) {
            fail(String.format("expected : %s but found %s", expectedToken.toString(), token.toString()));
        }
    }

    public static void assertListEquals(List<Object> expectedList, List<String> actualList) {
        assertNotNull(actualList);

        for (int i = 0; i < expectedList.size() && i < actualList.size(); i++) {
            if (!expectedList.get(i).equals(actualList.get(i))) {
                fail(String.format("expected '%s' but found '%s'", expectedList.get(i).toString(), actualList.get(i)));
            }
        }
        if (expectedList.size() > actualList.size()) {
            fail(String.format("expected '%s' not present in actual", expectedList.get(expectedList.size() - 1)));
        }
        if (expectedList.size() < actualList.size()) {
            fail(String.format("actual '%s' not present in expected", actualList.get(actualList.size() - 1)));
        }
    }
}

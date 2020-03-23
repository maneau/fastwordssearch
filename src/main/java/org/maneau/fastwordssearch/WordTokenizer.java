package org.maneau.fastwordssearch;

import java.util.*;

import static java.util.Collections.emptySet;
import static org.maneau.fastwordssearch.WordUtils.unaccentChar;

public class WordTokenizer {

    private static final String AZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final boolean[] isAsciiTab = new boolean[255];
    private Set<Character> customIncludeChar;
    private boolean includeAccent;
    private String text;
    private int pos = 0;
    private int start = 0;
    private int end = 0;
    private boolean openScript = false;

    static {
        generateIsAsciiTable();
    }

    private static void generateIsAsciiTable() {
        Arrays.fill(isAsciiTab, false);
        String az = AZ.toLowerCase();
        for (int i = 0; i < AZ.length(); i++) {
            isAsciiTab[AZ.charAt(i)] = true;
            isAsciiTab[az.charAt(i)] = true;
        }
    }

    public WordTokenizer(boolean includeAccent, char[] includeChar) {
        this.includeAccent = includeAccent;
        this.customIncludeChar = generateIsCustomTab(includeChar);
    }

    public static Builder builder() {
        return new Builder();
    }

    private Set<Character> generateIsCustomTab(char[] includeChar) {
        if (includeChar == null) {
            return emptySet();
        }
        Set<Character> map = new HashSet<>(includeChar.length);
        for (char c : includeChar) {
            map.add(c);
        }
        return map;
    }

    public WordTokenizer tokenize(String text) {
        this.text = text;
        this.pos = 0;
        this.start = 0;
        this.end = 0;
        return this;
    }

    private boolean isAnAsciiCharacter(char c) {
        if (customIncludeChar.contains(c)) {
            return true;
        }
        if (c >= 255) return false;
        if (includeAccent) {
            return isAsciiTab[unaccentChar(c)];
        } else {
            return isAsciiTab[c];
        }
    }

    public boolean find() {
        start = getNextAsciiPos();
        end = getNextNonAsciiPos();
        return start != -1;
    }

    private int getNextAsciiPos() {
        for (; pos < text.length(); pos++) {
            final char c = text.charAt(pos);
            if (c == '<') {
                openScript = true;
            } else if (openScript && c == '>') {
                openScript = false;
            } else if (!openScript && isAnAsciiCharacter(c)) {
                return pos;
            }
        }
        return -1;
    }

    private int getNextNonAsciiPos() {
        for (; pos < text.length(); pos++) {
            char c = text.charAt(pos);
            if (!isAnAsciiCharacter(c)) {
                return pos;
            }
        }
        return text.length();
    }

    public String getToken() {
        return this.text.substring(this.start, this.end);
    }

    public int end() {
        return end;
    }

    public int start() {
        return start;
    }

    public boolean find(int currentPos) {
        pos = currentPos;
        return find();
    }

    public List<String> getTokens() {
        List<String> tokens = new ArrayList<>();

        while (this.find()) {
            tokens.add(getToken());
        }
        return tokens;
    }

    public static class Builder {
        boolean includeAccent = false;
        char[] includeChar;

        public Builder includeAccent() {
            this.includeAccent = true;
            return this;
        }

        public Builder includingChars(char[] includeChar) {
            this.includeChar = includeChar;
            return this;
        }

        public Builder ignoreAccent(boolean isIgnoreAccent) {
            this.includeAccent = isIgnoreAccent;
            return this;
        }

        public WordTokenizer build() {
            return new WordTokenizer(this.includeAccent, this.includeChar);
        }
    }
}

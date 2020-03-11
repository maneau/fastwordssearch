package org.maneau.fastwordssearch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WordTokenizer {

    private static final String AZ = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private final String text;
    private static final boolean[] isAsciiTab = new boolean[255];

    private int pos = 0;
    private int start = 0;
    private int end = 0;
    private boolean openScript = false;

    static {
        Arrays.fill(isAsciiTab, false);
        String az = AZ.toLowerCase();
        for (int i = 0; i < AZ.length(); i++) {
            isAsciiTab[AZ.charAt(i)] = true;
            isAsciiTab[az.charAt(i)] = true;
        }
    }

    public WordTokenizer(String text) {
        this.text = text;
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

    private boolean isAnAsciiCharacter(char c) {
        return c < 255 && isAsciiTab[c];
    }

    private int getNextNonAsciiPos() {
        for (; pos < text.length(); pos++) {
            char c = text.charAt(pos);
            if (c < 255 && !isAsciiTab[c]) {
                return pos;
            }
        }
        return text.length();
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

    private String getToken() {
        return this.text.substring(this.start, this.end);
    }
}

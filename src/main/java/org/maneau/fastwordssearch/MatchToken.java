package org.maneau.fastwordssearch;

import java.util.Objects;

public class MatchToken {
    private final String keyword;
    private final int end;
    private final int start;

    public MatchToken(int start, int end, String keyword) {
        this.start = start;
        this.end = end;
        this.keyword = keyword;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (!(o instanceof MatchToken)) {
            return false;
        }
        final MatchToken oToken = (MatchToken) o;
        return start == oToken.start
                && end == oToken.end
                && Objects.equals(keyword, oToken.keyword);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, keyword);
    }

    @Override
    public String toString() {
        return String.format("['%s' at (%s,%s)]", this.keyword, this.start, this.end);
    }

    public String getKeyword() {
        return this.keyword;
    }
}

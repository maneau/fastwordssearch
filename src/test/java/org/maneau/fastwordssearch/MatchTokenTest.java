package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;
import static org.maneau.fastwordssearch.TestUtils.assertTokenEquals;

public class MatchTokenTest {

    final MatchToken token = new MatchToken(1, 3, "keyword");

    @Test
    public void testToString() {
        assertEquals("['keyword' at (1,3)]", token.toString());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void testEquals() {
        assertTokenEquals(new MatchToken(1, 3, "keyword"), token);
        assertFalse(token.equals(new MatchToken(2, 3, "keyword")));
        assertFalse(token.equals(new MatchToken(1, 10, "keyword")));
        assertFalse(token.equals(new MatchToken(1, 3, "keywords")));
        assertTrue(token.equals(token));
        assertFalse(token.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(1, 3, "keyword"), token.hashCode());
        assertNotSame(Objects.hash(2, 3, "keyword"), token.hashCode());
        assertNotSame(Objects.hash(1, 4, "keyword"), token.hashCode());
        assertNotSame(Objects.hash(1, 4, "keywords"), token.hashCode());
    }

    @Test
    public void testGetKeyword() {
        assertEquals("keyword", token.getKeyword());
    }
}

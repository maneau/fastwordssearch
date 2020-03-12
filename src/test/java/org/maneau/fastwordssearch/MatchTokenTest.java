package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.Objects;

import static org.junit.Assert.*;
import static org.maneau.fastwordssearch.TestUtils.assertTokenEquals;

public class MatchTokenTest {

    private final MatchToken tokenTest1 = new MatchToken(1, 3, "keyword");

    @Test
    public void testToString() {
        assertEquals("['keyword' at (1,3)]", tokenTest1.toString());
    }

    @SuppressWarnings("SimplifiableJUnitAssertion")
    @Test
    public void testEquals() {
        assertTokenEquals(new MatchToken(1, 3, "keyword"), tokenTest1);
        assertFalse(tokenTest1.equals(new MatchToken(2, 3, "keyword")));
        assertFalse(tokenTest1.equals(new MatchToken(1, 10, "keyword")));
        assertFalse(tokenTest1.equals(new MatchToken(1, 3, "keywords")));
        //noinspection EqualsWithItself
        assertTrue(tokenTest1.equals(tokenTest1));
        assertFalse(tokenTest1.equals(new Object()));
    }

    @Test
    public void testHashCode() {
        assertEquals(Objects.hash(1, 3, "keyword"), tokenTest1.hashCode());
        assertNotSame(Objects.hash(2, 3, "keyword"), tokenTest1.hashCode());
        assertNotSame(Objects.hash(1, 4, "keyword"), tokenTest1.hashCode());
        assertNotSame(Objects.hash(1, 4, "keywords"), tokenTest1.hashCode());
    }

    @Test
    public void testGetKeyword() {
        assertEquals("keyword", tokenTest1.getKeyword());
    }
}

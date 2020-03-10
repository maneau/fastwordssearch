package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class WordTokenizerTest {

    private static String html = TestUtils.loadTextFile("/wikipedia.html");

    @Test
    public void find_basic() {
        String text = "Hello word like  usual a";
        String[] expectedTokens = {"Hello", "word", "like", "usual", "a"};

        WordTokenizer wordTokenizer = new WordTokenizer(text);
        assertArrayEquals(expectedTokens, wordTokenizer.getTokens().toArray());
    }

    @Test
    public void find_basic_with_dots() {
        String text = "Hello...word-like,\n\t  usual!";
        String[] expectedTokens = {"Hello", "word", "like", "usual"};

        WordTokenizer wordTokenizer = new WordTokenizer(text);
        assertArrayEquals(expectedTokens, wordTokenizer.getTokens().toArray());
    }

    @Test
    public void find_html() {
        String html = "<ul><li>golden hammer</li><li>analysis paralysis</li></ul>";
        String[] expectedTokens = {"golden", "hammer", "analysis", "paralysis"};
        //String[] expectedTokens = {"ul", "li", "golden", "hammer", "li", "li", "analysis", "paralysis", "li", "ul"};

        WordTokenizer wordTokenizer = new WordTokenizer(html);
        assertArrayEquals(expectedTokens, wordTokenizer.getTokens().toArray());
    }

    @Test
    public void find_large_html() {
        WordTokenizer wordTokenizer = new WordTokenizer(html);
        List<String> tokens = wordTokenizer.getTokens();
        System.out.println(tokens);
        assertEquals(2164, tokens.size());
    }
}

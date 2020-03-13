package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.*;
import static org.maneau.fastwordssearch.TestUtils.*;

public class WordTrieTest {
    private static final String wikipediaHtml = loadTextFile("/wikipedia.html");
    private static final String basicHtmlTest = "<ul><li>golden hammer</li><li>analysis not paralysis</li><li>analysis paralysis</li></ul>";
    private static final MatchToken ANALYSIS_PARALYSIS = new MatchToken(61, 79, "analysis paralysis");
    private static final MatchToken GOLDEN_HAMMER = new MatchToken(8, 21, "golden hammer");

    @Test
    public void addKeywordsWith2And4LevelsKeywords() {
        WordTrie trie = new WordTrie();
        trie.addKeyword("golden hammer");
        trie.addKeyword("golden eye");
        trie.addKeyword("");
        trie.addKeyword(null);

        assertNull("empty keyword should be ignored",trie.getNode(""));
        assertNotNull("golden keyword should be present", trie.getNode("golden"));
        assertEquals(2, trie.size());
        assertEquals(2, trie.getNode("golden").size());

        trie.addKeyword("copy and paste programming");
        assertEquals(3, trie.size());
        assertEquals(2, trie.getNode("golden").size());

        trie.addKeyword("copy and paste programming");
        assertEquals("double keywords doesn't change the count", 3, trie.size());
    }

    @Test
    public void builderAddKeywords() {
        String[] keywords = {"golden hammer", "analysis paralysis"};
        WordTrie trie = WordTrie.builder().addKeywords(asList(keywords)).build();
        List<MatchToken> tokens = trie.parseText(basicHtmlTest);

        assertEquals(2, tokens.size());
        assertTokenEquals(GOLDEN_HAMMER, tokens.get(0));
        assertTokenEquals(ANALYSIS_PARALYSIS, tokens.get(1));
    }

    @Test
    public void parseTextWithNull() {
        WordTrie trie = WordTrie.builder().addKeyword("hammer").build();
        assertTrue(trie.parseText(null).isEmpty());
        assertTrue(trie.parseText("").isEmpty());
    }

    @Test
    public void parseTextWith1And2LevelsKeywords() {
        WordTrie trie = WordTrie.builder().addKeyword("hammer").build();
        List<MatchToken> tokens = trie.parseText("golden hammer");

        assertNotNull(trie.getNode("hammer"));
        assertEquals(1, tokens.size());
        assertTokenEquals(new MatchToken(7, 13, "hammer"),tokens.get(0));
    }

    @Test
    public void parseTextWith2And4Level() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("golden hammer")
                .addKeyword("golden eye")
                .addKeyword("copy and paste programming")
                .build();

        assertEquals(3, trie.size());
        List<MatchToken> tokens = trie.parseText("the movie golden eye is great. But don't copy of copy and paste programming");
        assertEquals(2, tokens.size());
        assertTokenEquals(new MatchToken(10,20, "golden eye"),tokens.get(0));
        assertTokenEquals(new MatchToken(58,75, "copy and paste programming"),tokens.get(1));
    }

    @Test
    public void parseTextWithBasicHtml() {
        String basicHtmlText = "<ul><li>golden hammer</li><li>analysis paralysis</li></ul>";
        WordTrie trie1 = WordTrie.builder().addKeyword("golden hammer").build();
        WordTrie trie2 = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = trie1.parseText(basicHtmlText);
        assertEquals(1, tokens.size());
        assertTokenEquals(GOLDEN_HAMMER, tokens.get(0));

        tokens = trie2.parseText(basicHtmlText);
        assertEquals(2, tokens.size());
        assertTokenEquals(GOLDEN_HAMMER, tokens.get(0));
        assertTokenEquals(new MatchToken(30, 48, "analysis paralysis"), tokens.get(1));
    }

    @Test
    public void parseTextWithHtml() {
        WordTrie trie = WordTrie.builder().ignoreCase().addKeyword("Analysis paralysis").build();
        String[] expectedKeyWord = {"Analysis paralysis"};

        assertListEquals(asList(expectedKeyWord), asList(toStringArray(trie.parseText("<b>Analysis paralysis</b>"))));
        assertListEquals(asList(expectedKeyWord), asList(toStringArray(trie.parseText("<b> Analysis</b> <i>paralysis</i>"))));
        assertTrue(trie.parseText("<p class=\" Analysis paralysis \">Hello</p>").isEmpty());
        assertTrue(trie.parseText("<!-- Analysis paralysis --> Hello").isEmpty());
    }

    @Test
    public void parseTextWithComplexHtmlAndValidPosition() {
        String basicHtmlText = "<p class=\"golden hammer\">golden not hammer</p><p>golden hammer</p></li><li>analysis not paralysis</li><li>analysis paralysis</li></ul>";
        WordTrie trie = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = trie.parseText(basicHtmlText);
        assertEquals(2, tokens.size());
        assertTokenEquals(new MatchToken(49, 62, "golden hammer"), tokens.get(0));
        assertTokenEquals(new MatchToken(106, 124, "analysis paralysis"), tokens.get(1));
    }

    @Test
    public void parseTextWithLargeHtmlPage() {
        WordTrie trie = WordTrie.builder().ignoreCase()
                .addKeyword("Analysis paralysis")
                .addKeyword("Bicycle shed")
                .addKeyword("Stovepipe or Silos")
                .addKeyword("Vendor lock-in")
                .addKeyword("Smoke and mirrors")
                .addKeyword("Copy and paste programming")
                .addKeyword("Golden hammer")
                .build();

        List<MatchToken> tokens = trie.parseText(wikipediaHtml);
        assertEquals(7, tokens.size());
        assertTokenEquals(new MatchToken(1571, 1589, "Analysis paralysis"), tokens.get(0));
        assertTokenEquals(new MatchToken(7136, 7154, "Analysis paralysis"), tokens.get(1));
        assertTokenEquals(new MatchToken(7393, 7405, "Bicycle shed"), tokens.get(2));
        assertTokenEquals(new MatchToken(10126, 10134, "Stovepipe or Silos"), tokens.get(3));
        assertTokenEquals(new MatchToken(12450, 12461, "Smoke and mirrors"), tokens.get(4));
        assertTokenEquals(new MatchToken(24355, 24372, "Copy and paste programming"), tokens.get(5));
        assertTokenEquals(new MatchToken(25157, 25170, "Golden hammer"), tokens.get(6));
    }

    @Test
    public void parseTextSimpleHtmlAndValidPosition() {
        WordTrie trie = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = trie.parseText(basicHtmlTest);
        assertEquals(2, tokens.size());
        assertTokenEquals(GOLDEN_HAMMER, tokens.get(0));
        assertTokenEquals(ANALYSIS_PARALYSIS, tokens.get(1));
    }

    @Test
    public void addkeywordsVerify() {
        String[] keywords = {"golden hammer", "analysis paralysis"};
        WordTrie trie = WordTrie.builder().build();
        trie.addKeywords(asList(keywords));
        List<MatchToken> tokens = trie.parseText(basicHtmlTest);

        assertEquals(2, tokens.size());
        assertTokenEquals(GOLDEN_HAMMER, tokens.get(0));
        assertTokenEquals(ANALYSIS_PARALYSIS, tokens.get(1));
    }

    @Test
    public void ignoreAccentVerify() {
        WordTrie trie = WordTrie.builder().ignoreAccent().addKeyword("déjà vu").build();
        MatchToken expectedToken = new MatchToken(7, 14, "déjà vu");

        assertListEquals(singletonList(expectedToken), trie.parseText("It's a deja vù"));
        assertTrue(trie.parseText("It's a deja Vu").isEmpty());
    }

    @Test
    public void ignoreAccentAndCase() {
        WordTrie trie = WordTrie.builder().ignoreAccent().ignoreCase().addKeyword("Déjà VÛ").build();
        MatchToken expectedToken = new MatchToken(7, 14, "Déjà VÛ");

        assertListEquals(singletonList(expectedToken), trie.parseText("It's a dËJa Vù"));
        assertListEquals(singletonList(expectedToken), trie.parseText("It's a dËJa-Vù"));
        assertTrue(trie.parseText("It's a deja not Vu").isEmpty());
    }
}
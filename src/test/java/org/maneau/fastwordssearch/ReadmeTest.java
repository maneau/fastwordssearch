package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.maneau.fastwordssearch.TestUtils.assertTokenEquals;

public class ReadmeTest {

    @Test
    public void basicUsage() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("text where searching some words SOME WORDS dont't");
        assertEquals(1, tokens.size());
        assertTokenEquals(new MatchToken(21,31,"some words"), tokens.get(0));
    }

    @Test
    public void caseInsensitive() {
        WordTrie trie = WordTrie.builder().ignoreCase()
                .addKeyword("Some wordS")
                .build();

        List<MatchToken> tokens = trie.parseText("text where searching some Words");

        assertEquals(1, tokens.size());
        assertTokenEquals(new MatchToken(21,31,"Some wordS"), tokens.get(0));
    }

    @Test
    public void accentInsensitive () {
        WordTrie trie = WordTrie.builder().ignoreAccent()
                .addKeyword("Déjà vu")
                .build();

        List<MatchToken> tokens = trie.parseText("it's like a Déja vü");

        assertEquals(1, tokens.size());
        assertTokenEquals(new MatchToken(12,19,"Déjà vu"), tokens.get(0));
    }

    @Test
    public void addKeywordsAfterBuild() {
        WordTrie trie = WordTrie.builder().ignoreCase()
                .addKeyword("some words")
                .build();

        trie.addKeyword("text where");

        List<MatchToken> tokens = trie.parseText("text where searching some words");
        assertEquals(2, tokens.size());
        assertTokenEquals(new MatchToken(0,10,"text where"), tokens.get(0));
        assertTokenEquals(new MatchToken(21,31,"some words"), tokens.get(1));
    }

    @Test
    public void ignoringSpecialCharactersAndPonctuation() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("this is some-words, and some...words as well");
        assertEquals(2, tokens.size());
        assertTokenEquals(new MatchToken(8,18,"some words"), tokens.get(0));
        assertTokenEquals(new MatchToken(24,36,"some words"), tokens.get(1));
    }

    @Test
    public void ignoringHtmlTags() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("<p class=\"some words\">some <b>words</b></p>");
        assertEquals(1, tokens.size());
        assertTokenEquals(new MatchToken(22,35,"some words"), tokens.get(0));
    }
}

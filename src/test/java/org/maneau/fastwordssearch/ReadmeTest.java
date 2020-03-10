package org.maneau.fastwordssearch;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class ReadmeTest {

    @Test
    public void test_basic() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("text where searching some words SOME WORDS dont't");
        assertEquals(1, tokens.size());
        assertEquals(new MatchToken(21,31,"some words"), tokens.get(0));
    }

    @Test
    public void test_ignoreCase() {
        WordTrie trie = WordTrie.builder().ignoreCase()
                .addKeyword("Some wordS")
                .build();

        List<MatchToken> tokens = trie.parseText("text where searching some Words");

        assertEquals(1, tokens.size());
        assertEquals(new MatchToken(21,31,"Some wordS"), tokens.get(0));
    }

    @Test
    public void test_post_addkeywords() {
        WordTrie trie = WordTrie.builder().ignoreCase()
                .addKeyword("some words")
                .build();

        trie.addKeyword("text where");

        List<MatchToken> tokens = trie.parseText("text where searching some words");
        assertEquals(2, tokens.size());
        assertEquals(new MatchToken(0,10,"text where"), tokens.get(0));
        assertEquals(new MatchToken(21,31,"some words"), tokens.get(1));
    }

    @Test
    public void test_ignore_specialchars() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("this is some-words, and some...words as well");
        assertEquals(2, tokens.size());
        assertEquals(new MatchToken(8,18,"some words"), tokens.get(0));
        assertEquals(new MatchToken(24,36,"some words"), tokens.get(1));
    }

    @Test
    public void test_ignore_html_tag() {
        WordTrie trie = WordTrie.builder()
                .addKeyword("some words")
                .build();

        List<MatchToken> tokens = trie.parseText("<p class=\"some words\">some <b>words</b></p>");
        assertEquals(1, tokens.size());
        assertEquals(new MatchToken(22,35,"some words"), tokens.get(0));
    }
}

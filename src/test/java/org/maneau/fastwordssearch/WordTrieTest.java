package org.maneau.fastwordssearch;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import static org.junit.Assert.*;
import static org.maneau.fastwordssearch.TestUtils.toStringArray;

public class WordTrieTest {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);
    private String wikipediaHtml = TestUtils.loadTextFile("/wikipedia.html");

    @Test
    public void addKeywords_add_2_levels() {
        WordTrie dico = new WordTrie();
        dico.addKeyword("manuel vals");
        dico.addKeyword("manuel petit");
        dico.addKeyword("");

        assertNotNull(dico.getNode("manuel"));
        assertEquals(2, dico.getNode("manuel").size());
        assertEquals(2, dico.size());

        dico.addKeyword("manuel petit");
        assertEquals(2, dico.size());
    }

    @Test
    public void search_basic() throws Exception {
        WordTrie dico = WordTrie.builder()
                .addKeyword("manuel vals")
                .addKeyword("manuel petit")
                .addKeyword("jean emmanuel chain")
                .addKeyword("eric petit")
                .build();

        assertNotNull(dico.getNode("manuel"));
        assertEquals(1, dico.parseText("ici eric petit manuel nouveau").size());
        assertEquals(2, dico.parseText("ici eric petit manuel nouveau jean emmanuel chain fin").size());
        assertEquals(2, dico.parseText("ici eric petit manuel nouveau jean emmanuel chain").size());
    }

    @Test
    public void parseText_in_large_html_page() throws Exception {
        WordTrie dico = WordTrie.builder()
                .addKeyword("Analysis paralysis")
                .addKeyword("Bicycle shed")
                .addKeyword("Stovepipe or Silos")
                .addKeyword("Vendor lock-in")
                .addKeyword("Smoke and mirrors")
                .addKeyword("Copy and paste programming")
                .addKeyword("Golden hammer")
                .build();

        List<MatchToken> tokens = dico.parseText(wikipediaHtml);
        assertEquals(7, tokens.size());
        assertEquals(new MatchToken(1550,1568,"Analysis paralysis"), tokens.get(0));
        assertEquals(new MatchToken(7115,7133,"Analysis paralysis"), tokens.get(1));
        assertEquals(new MatchToken(7372,7384,"Bicycle shed"), tokens.get(2));
        assertEquals(new MatchToken(10105,10113,"Stovepipe or Silos"), tokens.get(3));
        assertEquals(new MatchToken(12429,12440,"Smoke and mirrors"), tokens.get(4));
        assertEquals(new MatchToken(24334,24351,"Copy and paste programming"), tokens.get(5));
        assertEquals(new MatchToken(25136,25149,"Golden hammer"), tokens.get(6));
    }

    @Test
    public void parseText_basic_html() throws Exception {
        WordTrie dico = WordTrie.builder().ignoreCase()
                .addKeyword("Analysis paralysis")
                .build();

        String[] expectedKeyWord = {"Analysis paralysis"};
        assertArrayEquals(expectedKeyWord, toStringArray(dico.parseText("<b>Analysis paralysis</b>")));
        assertArrayEquals(expectedKeyWord, toStringArray(dico.parseText("<b> Analysis</b> <i>paralysis</i>")));
        assertTrue(dico.parseText("<p class=\" Analysis paralysis \">Hello</p>").isEmpty());
        assertTrue(dico.parseText("<!-- Analysis paralysis --> Hello").isEmpty());
    }

    @Test
    public void parseText_basic_html_valid_position() throws Exception {
        String basicHtmlText = "<ul><li>golden hammer</li><li>analysis paralysis</li></ul>";
        WordTrie dico1 = WordTrie.builder().addKeyword("golden hammer").build();
        WordTrie dico2 = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = dico1.parseText(basicHtmlText);
        assertEquals(1, tokens.size());
        assertEquals(new MatchToken(8,21,"golden hammer"), tokens.get(0));

        tokens = dico2.parseText(basicHtmlText);
        assertEquals(2, tokens.size());
        assertEquals(new MatchToken(8,21,"golden hammer"), tokens.get(0));
        assertEquals(new MatchToken(30,48,"analysis paralysis"), tokens.get(1));
    }

    @Test
    public void parseText_complexe_html_valid_position() throws Exception {
        String basicHtmlText = "<p class=\"golden hammer\">golden not hammer</p><p>golden hammer</p></li><li>analysis not paralysis</li><li>analysis paralysis</li></ul>";
        WordTrie dico = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = dico.parseText(basicHtmlText);
        assertEquals(2, tokens.size());
        assertEquals(new MatchToken(49,62,"golden hammer"), tokens.get(0));
        assertEquals(new MatchToken(106,124,"analysis paralysis"), tokens.get(1));
    }

    @Test
    public void parseText_simple_html_valid_position() throws Exception {
        String basicHtmlText = "<ul><li>golden hammer</li><li>analysis not paralysis</li><li>analysis paralysis</li></ul>";
        WordTrie dico = WordTrie.builder().addKeyword("golden hammer").addKeyword("analysis paralysis").build();

        List<MatchToken> tokens = dico.parseText(basicHtmlText);
        assertEquals(2, tokens.size());
        assertEquals(new MatchToken(8,21,"golden hammer"), tokens.get(0));
        assertEquals(new MatchToken(61,79,"analysis paralysis"), tokens.get(1));
    }
}
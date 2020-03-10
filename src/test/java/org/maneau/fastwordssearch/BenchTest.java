package org.maneau.fastwordssearch;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

public class BenchTest {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);
    private static final List<String> dictionnayOfWords = TestUtils.loadFirstNameFile();
    private static final Integer[] trieSizes = {100, 1000, 10000, 100000};
    private static final int NB_SEARCH = 100;
    private static final String[] keywords = {"Analysis paralysis", "Bicycle shed", "Stovepipe or Silos", "Vendor lock-in", "Smoke and mirrors", "Copy and paste programming", "Golden hammer"};
    private static final String wikipediaHtml = TestUtils.loadTextFile("/wikipedia.html");
    private Random rand = new Random();

    @Test
    public void testLoadFastWordSearch() {
        WordTrie wordTrie;
        for (int nb : trieSizes) {
            long start = System.currentTimeMillis();
            wordTrie = createNbelementDico(nb);
            long end = System.currentTimeMillis();

            LOG.info("Using WordTrie inject bench of {} elements in {}ms stored in {}",
                    nb,
                    (end - start),
                    -1);//RamUsageEstimator.humanSizeOf(wordTrie));

            //Searching part
            start = System.currentTimeMillis();
            List<MatchToken> tokens = null;

            for (int i = 0; i < NB_SEARCH; i++) {
                tokens = wordTrie.parseText(wikipediaHtml);
                assertEquals(7, tokens.size());
            }
            end = System.currentTimeMillis();
            LOG.info("Using WordTrie search find {} elements in {}ms with dictionnary of {} elements",
                    tokens.size(),
                    (end - start) / NB_SEARCH,
                    nb);
            System.out.println(TestUtils.toString(tokens));
        }
    }

    @Test
    public void testLoadAho() {
        for (int nb : trieSizes) {
            long start = System.currentTimeMillis();
            Trie trie = createNbelementDicoAho(nb);
            long end = System.currentTimeMillis();
            LOG.info("Using aho-corrasick inject bench of {} elements in {}ms stored in {}",
                    nb,
                    (end - start),
                    -1);//RamUsageEstimator.humanSizeOf(trie));

            //Searching part
            start = System.currentTimeMillis();
            Collection<Emit> tokens = null;

            for (int i = 0; i < NB_SEARCH; i++) {
                tokens = trie.parseText(wikipediaHtml);
                assertEquals(16, tokens.size());
            }
            end = System.currentTimeMillis();
            LOG.info("Using aho-corrasick search find {} elements in {}ms with dictionnary of {} elements",
                    tokens.size(),
                    (end - start) / NB_SEARCH,
                    nb);
        }
    }

    private WordTrie createNbelementDico(long nb) {
        WordTrie wordTrie = WordTrie.builder().ignoreCase().build();

        wordTrie.addKeywords(asList(keywords));
        while (wordTrie.size() < nb) {
            wordTrie.addKeyword(generateRandomKeyword());
        }

        return wordTrie;
    }

    private Trie createNbelementDicoAho(long nb) {
        Trie.TrieBuilder builder = Trie.builder().onlyWholeWords().ignoreCase();
        builder.addKeywords(asList(keywords));

        for (int i = 0; i < nb; i++) {
            builder.addKeyword(generateRandomKeyword());
        }

        return builder.build();
    }

    private String generateRandomKeyword() {
        StringBuilder sb = new StringBuilder();
        sb.append(dictionnayOfWords.get(rand.nextInt(dictionnayOfWords.size())));
        sb.append(" ");
        sb.append(dictionnayOfWords.get(rand.nextInt(dictionnayOfWords.size())));
        return sb.toString();
    }
}

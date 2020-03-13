package org.maneau.fastwordssearch;

import com.carrotsearch.sizeof.RamUsageEstimator;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

@Ignore
public class BenchTest {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);
    private static final List<String> dictionaryOfWords = TestUtils.loadFirstNameFile();
    private static final Integer[] trieSizes = {100, 1000, 10000, 100000, 1000000};
    private static final int NB_SEARCH = 100;
    private static final String[] keywords = {"Analysis paralysis", "Bicycle shed", "Stovepipe or Silos", "Vendor lock-in", "Smoke and mirrors", "Copy and paste programming", "Golden hammer"};
    private static final String wikipediaHtml = TestUtils.loadTextFile("/wikipedia.html");
    private final Random rand = new Random();

    @Test
    public void benchFastWordSearch() {
        WordTrie wordTrie;
        for (int nb : trieSizes) {
            long start = System.currentTimeMillis();
            wordTrie = createNbelementDico(nb);
            long end = System.currentTimeMillis();

            LOG.info("Using WordTrie inject bench of {} elements in {}ms stored in {}",
                    nb,
                    (end - start),
                    RamUsageEstimator.humanSizeOf(wordTrie));

            //Searching part
            start = System.currentTimeMillis();
            List<MatchToken> tokens = null;

            for (int i = 0; i < NB_SEARCH; i++) {
                tokens = wordTrie.parseText(wikipediaHtml);
                assertEquals(7, tokens.size());
            }
            end = System.currentTimeMillis();
            LOG.info("Using WordTrie search {} terms in {} docs during {}ms. With a dictionnary containing {} elements",
                    tokens.size(),
                    NB_SEARCH,
                    (end - start),
                    nb);
            LOG.info("tokens : {}", TestUtils.toString(tokens));
        }
    }

    @Test
    public void benchAhoCorasick() {
        for (int nb : trieSizes) {
            long start = System.currentTimeMillis();
            Trie trie = createNbelementDicoAho(nb);
            long end = System.currentTimeMillis();
            LOG.info("Using aho-corasick inject bench of {} elements in {}ms stored in {}",
                    nb,
                    (end - start),
                    RamUsageEstimator.humanSizeOf(trie));

            //Searching part
            start = System.currentTimeMillis();
            Collection<Emit> tokens = null;

            for (int i = 0; i < NB_SEARCH; i++) {
                tokens = trie.parseText(wikipediaHtml);
                assertEquals(16, tokens.size());
            }
            end = System.currentTimeMillis();
            LOG.info("Using aho-corasick search {} terms in {} docs during {}ms. With a dictionnary containing {} elements",
                    tokens.size(),
                    NB_SEARCH,
                    (end - start),
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

    @SuppressWarnings({"ConstantConditions", "StringBufferReplaceableByString"})
    private String generateRandomKeyword() {
        StringBuilder sb = new StringBuilder();
        sb.append(dictionaryOfWords.get(rand.nextInt(dictionaryOfWords.size())));
        sb.append(" ");
        sb.append(dictionaryOfWords.get(rand.nextInt(dictionaryOfWords.size())));
        return sb.toString();
    }
}

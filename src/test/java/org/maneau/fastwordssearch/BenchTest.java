package org.maneau.fastwordssearch;

import com.carrotsearch.sizeof.RamUsageEstimator;
import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

import static java.util.Arrays.asList;

public class BenchTest {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);
    private Random rand = new Random();
    private List<String> dictionnayOfWords = loadFirstNameFile();
    private Integer[] trieSizes = {100, 1000, 10000, 100000};
    private String[] keywords = {"Analysis paralysis", "Bicycle shed", "Stovepipe or Silos", "Vendor lock-in", "Smoke and mirrors", "Copy and paste programming", "Golden hammer"};
    private String wikipediaHtml = loadTextFile("/wikipedia.html");

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
                    RamUsageEstimator.humanSizeOf(wordTrie));

            //Searching part
            start = System.currentTimeMillis();
            List<MatchToken> tokens = wordTrie.parseText(wikipediaHtml);
            end = System.currentTimeMillis();
            LOG.info("Using WordTrie search find {} elements in {}ms with dictionnary of {} elements",
                    tokens.size(),
                    (end - start),
                    nb);
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
                    RamUsageEstimator.humanSizeOf(trie));

            //Searching part
            start = System.currentTimeMillis();
            Collection<Emit> tokens = trie.parseText(wikipediaHtml);
            end = System.currentTimeMillis();
            LOG.info("Using aho-corrasick search find {} elements in {}ms with dictionnary of {} elements",
                    tokens.size(),
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

    private String generateRandomKeyword() {
        StringBuilder sb = new StringBuilder();
        sb.append(dictionnayOfWords.get(rand.nextInt(dictionnayOfWords.size())));
        sb.append(" ");
        sb.append(dictionnayOfWords.get(rand.nextInt(dictionnayOfWords.size())));
        return sb.toString();
    }

    private List<String> loadFirstNameFile() {
        String fileName = "/firstname.lst";
        List<String> keywords = new ArrayList<String>();

        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            LOG.error("File not found " + fileName);
            return null;
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                keywords.add(scanner.nextLine());
            }
        }
        return keywords;
    }

    private String loadTextFile(String fileName) {
        StringBuilder sbText = new StringBuilder();

        InputStream inputStream = this.getClass().getResourceAsStream(fileName);
        if (inputStream == null) {
            LOG.error("File not found " + fileName);
            return null;
        }
        try (Scanner scanner = new Scanner(inputStream)) {
            while (scanner.hasNext()) {
                sbText.append(scanner.nextLine()).append('\n');
            }
        }
        return sbText.toString();
    }
}

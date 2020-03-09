package org.maneau.fastwordssearch;

import com.carrotsearch.sizeof.RamUsageEstimator;
import org.ahocorasick.trie.Trie;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class BenchTest {
    private static final Logger LOG = LoggerFactory.getLogger(BenchTest.class);

    @Test
    public void testLoad() {
        Integer[] sizes = {100, 1000, 10000, 100000, 1000000};
        for (int nb : sizes) {
            long start = System.currentTimeMillis();
            WordTrie dico = createNbelementDico(nb);
            String memSize = RamUsageEstimator.humanSizeOf(dico);

            long end = System.currentTimeMillis();
            LOG.info("Using WordTrie inject bench of {} elements in {}ms stored in {}", nb, (end - start), memSize);
        }
    }

    @Test
    public void testLoadAho() {
        Integer[] sizes = {100, 1000, 10000, 100000};
        for (int nb : sizes) {
            long start = System.currentTimeMillis();
            Trie trie = createNbelementDicoAho(nb);
            String memSize = RamUsageEstimator.humanSizeOf(trie);

            long end = System.currentTimeMillis();
            LOG.info("Using AhoCorasick Inject bench of {} elements in {}ms stored in {}", nb, (end - start), memSize);
        }
    }

    private WordTrie createNbelementDico(long nb) {
        WordTrie wordTrie = WordTrie.builder().ignoreCase().build();
        List<String> firstNames = loadFirstNameFile();
        final Random rand = new Random();
        final Integer max = firstNames.size();

        while (wordTrie.size() < nb) {
            StringBuilder sb = new StringBuilder();
            sb.append(firstNames.get(rand.nextInt(max)));
            sb.append(" ");
            sb.append(firstNames.get(rand.nextInt(max)));
            //String keyword = firstNames.get(rand.nextInt(max)) + " " + firstNames.get(rand.nextInt(max));
            wordTrie.addKeyword(sb.toString());
        }

        return wordTrie;
    }

    private Trie createNbelementDicoAho(long nb) {
        Trie.TrieBuilder builder = Trie.builder().onlyWholeWords().ignoreCase();
        List<String> firstNames = loadFirstNameFile();
        final Random rand = new Random();
        final Integer max = firstNames.size();

        for(int i =0; i < nb ; i++) {
            StringBuilder sb = new StringBuilder();
            sb.append(firstNames.get(rand.nextInt(max)));
            sb.append(" ");
            sb.append(firstNames.get(rand.nextInt(max)));
            //String keyword = firstNames.get(rand.nextInt(max)) + " " + firstNames.get(rand.nextInt(max));
            builder.addKeyword(sb.toString());
        }

        return builder.build();
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
}

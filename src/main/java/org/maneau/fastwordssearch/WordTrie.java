package org.maneau.fastwordssearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.maneau.fastwordssearch.WordUtils.*;

@SuppressWarnings("rawtypes")
public class WordTrie {
    private static final Logger LOG = LoggerFactory.getLogger(WordTrie.class);
    private static final String END = "§";

    private final Map<String, Map> nodes = new HashMap<>();
    private boolean isIgnoreCase;
    private boolean isIgnoreAccent;
    private Long numberOfKeywords = 0L;

    public void addKeyword(String keyword) {
        if (hasNoText(keyword)) {
            String transcodeKeyword = keyword;
            if (isIgnoreAccent) {
                transcodeKeyword = unaccent(transcodeKeyword);
            }
            if (isIgnoreCase) {
                transcodeKeyword = transcodeKeyword.toLowerCase();
            }
            addKeyword(nodes, transcodeKeyword, keyword);
        }
    }

    public static TrieBuilder builder() {
        return new TrieBuilder();
    }

    public long size() {
        return numberOfKeywords;
    }

    private boolean isEndOfBranch(Map<String, Map> subNode) {
        //noinspection unchecked
        return isWordFounded(subNode) && isWordFounded(subNode.get(END));
    }

    public List<MatchToken> parseText(final String inputText) {
        if (hasText(inputText)) {
            return emptyList();
        }
        LOG.debug("Starting parsing text of {} characters", inputText.length());

        String text = inputText;
        if (isIgnoreAccent) {
            text = unaccent(text);
        }
        if (isIgnoreCase) {
            text = text.toLowerCase();
        }

        WordTokenizer tokenizer = new WordTokenizer(text);
        List<MatchToken> tokens = parseText(nodes, tokenizer, text);

        LOG.debug("finish parsing text of {} characters and {} keywords",
                    text.length(), this.numberOfKeywords);
        return tokens;
    }

    private boolean isWordFounded(Map<String, Map> subNode) {
        return subNode != null;
    }

    private String getKeywordFromNode(Map<String, Map> currentNode) {
        return (String) currentNode.get(END).keySet().iterator().next();
    }

    private List<MatchToken> subParseText(final Map<String, Map> node, final WordTokenizer matcher, final int matchStartPos, int currentPos, final String text) {
        LOG.debug("subParseText at {} position with match started at {}", currentPos, matchStartPos);
        if (matcher.find(currentPos)) {
            final int endPos = matcher.end();
            final int startPos = matcher.start();
            final String currentWord = text.substring(startPos, endPos);

            @SuppressWarnings("unchecked") Map<String, Map> subNode = node.get(currentWord);
            if (isEndOfBranch(subNode)) {
                //end of branch, we got a result
                String matchStr = getKeywordFromNode(subNode);
                LOG.debug("find '{}' at ({},{}) position", matchStr, matchStartPos, endPos);
                return singletonList(new MatchToken(matchStartPos, endPos, matchStr));
            } else if (isWordFounded(subNode)) {
                //Search descending in the tree
                return subParseText(subNode, matcher, startPos, endPos, text);
            }
        }
        return emptyList();
    }

    public void addKeywords(List<String> keywords) {
        if (isNotEmptyList(keywords)) {
            for (String keyword : keywords) {
                addKeyword(keyword);
            }
        }
    }

    private List<MatchToken> parseText(final Map<String, Map> node, final WordTokenizer matcher, final String text) {
        LOG.trace("parseText {}", text);
        List<MatchToken> tokens = new ArrayList<>();
        int currentPos = 0;

        while (matcher.find(currentPos)) {
            final int endPos = matcher.end();
            final int startPos = matcher.start();
            final String currentWord = text.substring(startPos, endPos);

            LOG.debug("parseText {}", currentWord);

            @SuppressWarnings("unchecked") Map<String, Map> subNode = node.get(currentWord);
            if (isEndOfBranch(subNode)) {
                //end of the branch
                String matchStr = getKeywordFromNode(subNode);
                LOG.debug("find '{}' at ({},{}) position", matchStr, startPos, endPos);
                tokens.add(new MatchToken(startPos, endPos, matchStr));
            } else if (isWordFounded(subNode)) {
                //Search descending in the tree
                tokens.addAll(subParseText(subNode, matcher, startPos, endPos, text));
            }
            currentPos = endPos;
        }
        return tokens;
    }

    private void addKeyword(final Map<String, Map> currentNode, final String word, final String originalKeyword) {
        LOG.debug("adding the word '{}'", word);
        if (hasText(word)) {
            //their is no words left, store the original
            Map<String, Map> allWordMap = new HashMap<>();
            allWordMap.put(originalKeyword, null);

            if (!currentNode.containsKey(END)) {
                currentNode.put(END, allWordMap);
                numberOfKeywords++;
            } else {
                LOG.debug("Keyword '{}' already in dictionary", originalKeyword);
            }
        } else {
            //split the word if you can
            int indexFinMot = word.indexOf(" ");
            if (indexFinMot == -1) {
                indexFinMot = word.length();
            }

            String currentWord = word.substring(0, indexFinMot);
            String restOfWord = null;
            if (indexFinMot < word.length()) {
                restOfWord = word.substring(indexFinMot + 1);
            }

            @SuppressWarnings("unchecked") Map<String, Map> subNode = currentNode.computeIfAbsent(currentWord, k -> new HashMap<>());
            //recurs call
            addKeyword(subNode, restOfWord, originalKeyword);
        }
    }

    @SuppressWarnings("SameParameterValue")
    protected Map<String, Map> getNode(String keyword) {
        //noinspection unchecked
        return nodes.get(keyword);
    }

    private void ignoreCase(boolean b) {
        this.isIgnoreCase = b;
    }

    private void ignoreAccent(boolean b) {
        this.isIgnoreAccent = b;
    }

    /**
     * Inner class Builder
     */
    public static class TrieBuilder {

        private boolean ignoreCase = false;
        private boolean ignoreAccent = false;
        private final Set<String> keywords = new HashSet<>();

        public TrieBuilder ignoreCase() {
            this.ignoreCase = true;
            return this;
        }

        public TrieBuilder ignoreAccent() {
            this.ignoreAccent = true;
            return this;
        }

        public TrieBuilder addKeyword(String keyword) {
            keywords.add(keyword);
            return this;
        }

        public TrieBuilder addKeywords(List<String> keywordList) {
            keywords.addAll(keywordList);
            return this;
        }

        public WordTrie build() {
            WordTrie trie = new WordTrie();

            trie.ignoreCase(ignoreCase);
            trie.ignoreAccent(ignoreAccent);

            for (String keyword : keywords) {
                trie.addKeyword(keyword);
            }
            return trie;
        }
    }
}

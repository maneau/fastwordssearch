package org.maneau.fastwordssearch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Collections.emptyList;

public class WordTrie {
    private static final Logger LOG = LoggerFactory.getLogger(WordTrie.class);
    private static final String END = "§";
    private static final String regexpTokenSansHtml = "([().]|['\\w]+)(?![^<]*>)";

    private Map<String, Map> nodes = new HashMap<String, Map>();
    private boolean isIgnoreCase;
    private Long numberOfKeywords = 0L;

    public void addKeyword(String keyword) {
        if (isNotEmpty(keyword)) {
            if (isIgnoreCase) {
                addKeyword(nodes, keyword.toLowerCase(), keyword);
            } else {
                addKeyword(nodes, keyword, keyword);
            }
        }
    }

    public List<MatchToken> parseText(String text) {
        if (isEmpty(text)) {
            return emptyList();
        }
        long start = 0;
        if (LOG.isInfoEnabled()) {
            LOG.debug("Starting parsing text of {} characters", text.length());
            start = System.currentTimeMillis();
        }
        if (isIgnoreCase) {
            text = text.toLowerCase();
        }
        Matcher matcher = Pattern.compile(regexpTokenSansHtml).matcher(text);
        List<MatchToken> tokens = parseText(nodes, matcher, -1, 0, text);

        if (LOG.isInfoEnabled()) {
            long end = System.currentTimeMillis();
            LOG.info("finish parsing text of {} characters and {} keywords in {} ms", text.length(), this.numberOfKeywords, (end - start));
        }
        return tokens;
    }

    public long size() {
        return numberOfKeywords;
    }

    private List<MatchToken> parseText(final Map<String, Map> node, final Matcher matcher, final int matchStartPos, int currentPos, final String text) {
        if (LOG.isTraceEnabled()) {
            LOG.trace("searching at {} position with match started at {} in {}", currentPos, matchStartPos, text.substring(currentPos));
        }
        List<MatchToken> matchList = new ArrayList<>();

        if (matcher.find(currentPos)) {
            final int endPos = matcher.end();
            final int startPos = matcher.start();
            final String currentWord = text.substring(startPos, endPos);

            Map<String, Map> subNode = node.get(currentWord);
            if (subNode != null && subNode.get(END) != null) {
                //nous sommes en fin de branche
                String matchStr = (String) subNode.get(END).keySet().iterator().next();
                LOG.info("find '{}' at ({},{}) position", matchStr, matchStartPos, endPos);
                matchList.add(new MatchToken(matchStartPos, endPos, matchStr));
                return matchList;
            } else if (subNode != null) {
                //Search descending in the tree
                matchList.addAll(parseText(subNode, matcher, startPos, endPos, text));
            }
            if (matchStartPos == -1) {
                //Search the next word only if we are on the 1st level of searching
                matchList.addAll(parseText(nodes, matcher, -1, endPos, text));
            }
        }
        return matchList;
    }

    private boolean isNotEmpty(String word) {
        return !isEmpty(word);
    }

    private boolean isEmpty(String word) {
        if (word == null) return true;
        return word.isEmpty();
    }

    private void addKeyword(final Map<String, Map> currentNode, final String word, final String originalKeyword) {
        LOG.debug("adding the word '{}'", word);
        if (isEmpty(word)) {
            //their is no words left, store the original
            Map<String, Map> allWordMap = new HashMap<String, Map>();
            allWordMap.put(originalKeyword, null);

            if (!currentNode.containsKey(END)) {
                currentNode.put(END, allWordMap);
                numberOfKeywords++;
            } else {
                LOG.debug("Keyword '{}' allready in dico", originalKeyword);
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

            Map<String, Map> subNode = currentNode.get(currentWord);
            if (subNode == null) {
                subNode = new HashMap<String, Map>();
                currentNode.put(currentWord, subNode);
            }
            //recurssif call
            addKeyword(subNode, restOfWord, originalKeyword);
        }
    }

    public void addKeywords(List<String> keywords) {
        if (isNotEmpty(keywords)) {
            for (String keyword : keywords) {
                addKeyword(keyword);
            }
        }
    }

    private static boolean isNotEmpty(List<String> objects) {
        if (objects == null) {
            return true;
        }
        return !objects.isEmpty();
    }

    protected Map<String, Map> getNode(String keyword) {
        return nodes.get(keyword);
    }

    public static DicoBuilder builder() {
        return new DicoBuilder();
    }

    private void ignoreCase(boolean b) {
        this.isIgnoreCase = b;
    }

    /**
     * Inner class Builder
     */
    public static class DicoBuilder {

        private boolean ignoreCase = false;
        private Set<String> keywords = new HashSet<>();

        public DicoBuilder ignoreCase() {
            this.ignoreCase = true;
            return this;
        }

        public DicoBuilder addKeyword(String keyword) {
            keywords.add(keyword);
            return this;
        }

        public DicoBuilder addKeywords(List<String> keywordList) {
            keywords.addAll(keywordList);
            return this;
        }

        public WordTrie build() {
            WordTrie dico = new WordTrie();
            dico.ignoreCase(true);
            for (String keyword : keywords) {
                dico.addKeyword(keyword);
            }
            return dico;
        }
    }
}

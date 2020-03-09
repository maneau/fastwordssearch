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

    public List<String> parseText(String text) {
        if (isEmpty(text)) {
            return emptyList();
        }
        if (isIgnoreCase) {
            return parseText(nodes, text.toLowerCase());
        } else {
            return parseText(nodes, text);
        }
    }

    public long size() {
        return numberOfKeywords;
    }

    private List<String> parseText(Map<String, Map> node, String text) {
        List<String> matchList = new ArrayList<>();

        LOG.debug("search > {}", text);
        if (isEmpty(text)) {
            return matchList;
        }

        Matcher matcher = Pattern.compile(regexpTokenSansHtml).matcher(text);
        if (!matcher.find()) {
            return matchList;
        }

        int indexFinMot = matcher.end();
        final String currentWord = text.substring(matcher.start(), matcher.end());

        String restOfPhrase = null;
        if (indexFinMot < text.length()) {
            restOfPhrase = text.substring(indexFinMot);
        }
        Map<String, Map> subNode = node.get(currentWord);
        if (subNode != null && subNode.get(END) != null) {
            //nous sommes en fin de branche
            String matchStr = (String) subNode.get(END).keySet().iterator().next();
            LOG.info("trouve > {}", matchStr);
            matchList.add(matchStr);
        } else if (subNode != null) {
            //on descend dans l'arbre
            matchList.addAll(parseText(subNode, restOfPhrase));
        }
        //on passe on mot suivant
        matchList.addAll(parseText(node, restOfPhrase));
        return matchList;
    }

    private List<String> parseTextWithSpace(Map<String, Map> node, String text) {
        List<String> matchList = new ArrayList<>();

        LOG.debug("search > {}", text);
        if (isEmpty(text)) {
            return emptyList();
        }
        int indexFinMot = text.indexOf(" ");
        if (indexFinMot == -1) {
            indexFinMot = text.length();
        }
        String currentWord = text.substring(0, indexFinMot);
        String restOfPhrase = null;
        if (indexFinMot < text.length()) {
            restOfPhrase = text.substring(indexFinMot + 1);
        }
        Map<String, Map> subNode = node.get(currentWord);
        if (subNode != null && subNode.get(END) != null) {
            //nous sommes en fin de branche
            String matchStr = (String) subNode.get(END).keySet().iterator().next();
            LOG.info("trouve > {}", matchStr);
            matchList.add(matchStr);
        } else if (subNode != null) {
            //on descend dans l'arbre
            matchList.addAll(parseText(subNode, restOfPhrase));
        }
        //on passe on mot suivant
        matchList.addAll(parseText(node, restOfPhrase));
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

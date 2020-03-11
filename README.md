# FastWordsSearch

[![Build Status](https://travis-ci.org/maneau/fastwordssearch.svg?branch=master)](https://travis-ci.org/maneau/fastwordssearch)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/b3c3e839a4b34772af076cb763c67a0e)](https://www.codacy.com/manual/maneau/fastwordssearch?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=maneau/fastwordssearch&amp;utm_campaign=Badge_Grade)
[![Codecov](https://codecov.io/gh/maneau/fastwordssearch/branch/master/graph/badge.svg)](https://codecov.io/gh/maneau/fastwordssearch)
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.maneau/fastwordssearch/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.maneau/fastwordssearch)
[![Javadoc](https://javadoc-emblem.rhcloud.com/doc/org.maneau/fastwordssearch/badge.svg)](http://www.javadoc.io/doc/org.maneau/fastwordssearch)
[![Apache 2](http://img.shields.io/badge/license-Apache%202-blue.svg)](http://www.apache.org/licenses/LICENSE-2.0)

## Dependency

Coming soon ...

## Introduction

Algorithm for searching multi words in text.

Inspire from [aho-corasick](https://github.com/robert-bor/aho-corasick) it works almost like it.

Why creating a another ? 

It design to be less memory greedy and as much fast as aho-corasick.

How it works ?

This algorithm stores only word by word keywords on a tree and nothing more than String in Map.
Fast thanks to its tokenizer which reads sentences in one pass and ignores special characters or html tags.

Can you prouve it ?

Juste launch the BenchTest and you get something like this for a wikipedia html page :
 
|Dictionary Size | Aho-Corasick memory | FastWordsSearch memory | Aho-Corasick speed  | FastWordsSearch speed | 
|----------------|---------------------|------------------------|---------------------|-----------------------|
|100             | 274.6 KB            | 69.5 KB                | 191ms /100 docs     | 194ms /100 docs       |
|1000            | 2.3 MB              | 676.7 KB               | 127ms /100 docs     | 115ms /100 docs       |
|10000           | 18.3 MB             | 6 MB                   | 156ms /100 docs     | 89ms  /100 docs       |
|100000          | 135.5 MB            | 48 MB                  | 164ms /100 docs     | 52ms  /100 docs       |
|1000000         | 1.1 GB              | 463.6 MB               | 201ms /100 docs     | 46ms  /100 docs       |

for a txt page the result are very similar :

|Dictionary Size | Aho-Corasick memory | FastWordsSearch memory | Aho-Corasick speed  | FastWordsSearch speed | 
|----------------|---------------------|------------------------|---------------------|-----------------------|
|100             | 280.4 KB            | 69.8 KB                | 147ms /100 docs     | 118ms /100 docs       |
|1000            | 2.3 MB              | 678.9 KB               | 80ms  /100 docs     | 71ms  /100 docs       |
|10000           | 18.3 MB             | 6 MB                   | 133ms /100 docs     | 158ms /100 docs       |
|100000          | 135.4 MB            | 48 MB                  | 49ms  /100 docs     | 40ms  /100 docs       |
|1000000         | 1.1 GB              | 463.6 MB               | 110ms /100 docs     | 30ms  /100 docs       |

## Usage

### Basic usage
 
```java
WordTrie trie = WordTrie.builder()
        .addKeyword("some words")
        .build();

List<MatchToken> tokens = trie.parseText("text where searching some words SOME WORDS dont't");
assertEquals(1, tokens.size());
assertTokenEquals(new MatchToken(21,31,"some words"), tokens.get(0));
```

It while return "some words" and it place.

### Case Insensitive 

You can need to be case insensitive :
```java
WordTrie trie = WordTrie.builder().ignoreCase()
        .addKeyword("Some wordS")
        .build();

List<MatchToken> tokens = trie.parseText("text where searching some Words");

assertEquals(1, tokens.size());
assertTokenEquals(new MatchToken(21,31,"Some wordS"), tokens.get(0));
```

### Still can add keywords

Once build, you still can to add some keywords.

```java
WordTrie trie = WordTrie.builder().ignoreCase()
        .addKeyword("some words")
        .build();

trie.addKeyword("text where");

List<MatchToken> tokens = trie.parseText("text where searching some words");
assertEquals(1, tokens.size());
assertTokenEquals(new MatchToken(8,21,"some words"), tokens.get(0));
```

### Ignoring special characters and ponctuation

The library contains a simple but fast String Tokenizer which ignore special characters.

```java
WordTrie trie = WordTrie.builder()
        .addKeyword("some words")
        .build();

List<MatchToken> tokens = trie.parseText("this is some-words, and some...words as well");
assertEquals(2, tokens.size());
assertTokenEquals(new MatchToken(8,18,"some words"), tokens.get(0));
assertTokenEquals(new MatchToken(24,36,"some words"), tokens.get(1));
```

### Ignoring html tags

The library contains a simple but fast String Tokenizer which ignore html tags.

```java
WordTrie trie = WordTrie.builder()
    .addKeyword("some words")
    .build();

List<MatchToken> tokens = trie.parseText("<p class=\"some words\">some <b>words</b></p>");
assertEquals(1, tokens.size());
assertTokenEquals(new MatchToken(22,35,"some words"), tokens.get(0));
```

_**Note** : the Tokenizer only consider ASCII words neither numbers or special char `(-_/)`_ 
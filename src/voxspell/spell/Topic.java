package voxspell.spell;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Represents a topic of words in a word list.
 * @author Kevin Hira.
 */
public class Topic implements Serializable {
    private String _name;
    private Map<String, Word> _topicWords;

    public Topic(String topicName) {
        _name = topicName;
        _topicWords = new HashMap<String, Word>();
    }

    /**
     * Add a word to the topic.
     * @param word the word to add.
     */
    public void add(String word) {
        if (!_topicWords.containsKey(word)) {
            _topicWords.put(word, new Word(word));
        }
    }

    /**
     * Adds all words to the topic
     * @words the words to add.
     */
    public void addAll(List<String> words) {
        for (String word: words) {
            add(word);
        }
    }

    /**
     * Gets the words from this topic.
     * @return a list of all the words.
     */
    public List<Word> getWords() {
        List<Word> words = new ArrayList<Word>();
        for (Word w : _topicWords.values()) {
            words.add(w);
        }

        return words;
    }

    /**
     * Generates raw statistics for words in the topic.
     * @return list of raw statistics.
     */
    public List<String> rawStats() {
        List<String> stats = new ArrayList<String>();
        for (String word : _topicWords.keySet()) {
            String stat = "";
            stat += _topicWords.get(word).getMastered() + "\t";
            stat += _topicWords.get(word).getFaulted() + "\t";
            stat += _topicWords.get(word).getFailed() + "\t";
            stat += word;

            stats.add(stat);
        }

        return stats;
    }

    public String getName() {
        return _name;
    }

    // Adapted 206A3 SpellModel.
    /**
     * Retrieve words for a quiz.
     * @param n the amount of words.
     * @param isReview whether the quiz is a review or not.
     * @return list of words that can be used for a quiz.
     */
    public List<Word> getQuizWords(int n, boolean isReview) {
        List<Word> words = new ArrayList<Word>();
        _topicWords.values().stream().filter((word) -> (!isReview || word.isReview())).forEach((word) -> {
            words.add(word);
        });

        Collections.shuffle(words);
        if (words.size() >= n) {
            return new ArrayList<Word>(words.subList(0, n));
        }
        return words;
    }
}

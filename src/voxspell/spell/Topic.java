package voxspell.spell;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

public class Topic implements Serializable {
    private String _name;
    private Map<String, Word> _topicWords;

    public Topic(String topicName) {
        _name = topicName;
        _topicWords = new HashMap<String, Word>();
    }

    public void add(String word) {
        if (!_topicWords.containsKey(word)) {
            _topicWords.put(word, new Word(word));
        }
    }

    public void addAll(List<String> words) {
        for (String word: words) {
            add(word);
        }
    }

    public List<Word> getWords() {
        List<Word> words = new ArrayList<Word>();
        for (Word w : _topicWords.values()) {
            words.add(w);
        }

        return words;
    }

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
    public List<Word> getQuizWords(int n, boolean isReview) {
        List<Word> words = new ArrayList<Word>();
        _topicWords.values().stream().filter((word) -> (!isReview || word.isReview())).forEach((word) -> {
            words.add(word);
        });

        Collections.shuffle(words);
        if (words.size() >= n) {
            return words.subList(0, n);
        }
        return words;
    }
}

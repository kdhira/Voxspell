package voxspell.spell;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;

public class Quiz {
    private Date _quizDate;
    private List<Word> _quizWords;
    private LinkedList<SpellResult> _results;
    private Topic _topic;
    private int _nWords;
    private boolean _isReview;

    private boolean _isFinised;

    private int _wordIndex;
    private int _wordFlag;


    public Quiz(Topic topic, int nWords, boolean isReview) {
        _quizDate = new Date();

        _topic = topic;
        _nWords = nWords;
        _isReview = isReview;

        _wordIndex = 0;
        _wordFlag = 0;

        _results = new LinkedList<SpellResult>();

        _quizWords = _topic.getQuizWords(nWords, _isReview);
        _isFinised = numberWords() <= 0;
    }

    public QuizFlag doWork(String query) {
        if (_isFinised) {
            return QuizFlag.ALREADY_DONE;
        }

        if (checkWord(query)) {
            // Correct attempt.
            // Get next word.
            if (!nextWord()) {
                return QuizFlag.QUIZ_DONE;
            }

            return QuizFlag.NEW_WORD;
        }
        else if (++_wordFlag >= 2) {
            if (!nextWord()) {
                return QuizFlag.QUIZ_DONE;
            }

            return QuizFlag.NEW_WORD;
        }
        else {
            return QuizFlag.NO_CHANGE;
        }

        // return QuizFlag.INVALID;

    }

    public int numberWords() {
        return _quizWords.size();
    }

    public int currentIndex() {
        return _wordIndex;
    }

    public String currentWord() {
        return _quizWords.get(_wordIndex).toString();
    }

    public LinkedList<SpellResult> getResults() {
        return _results;
    }

    private boolean checkWord(String word) {
        return _quizWords.get(_wordIndex).toString().toLowerCase().equals(word.toLowerCase());
    }

    private boolean nextWord() {
        // _quizWords.get(_wordIndex).logStatistic(WordResult.parse(_wordFlag));
        _results.addLast(new SpellResult(currentWord(), WordResult.parse(_wordFlag)));
        _wordFlag = 0;
        if (++_wordIndex >= numberWords()) {
            _isFinised = true;
            return false;
        }
        return true;
    }

    public void logStatistics() {
        if (_quizWords.size() == _results.size()) {
            for (int i = 0; i < _quizWords.size(); ++i) {
                _quizWords.get(i).logStatistic(_results.get(i).getResult());
            }
        }
    }

    public Map<WordResult, Integer> retrieveTotals() {
        Map<WordResult, Integer> totals = new HashMap<WordResult, Integer>();
        for (SpellResult sR : _results) {
            if (!totals.containsKey(sR.getResult())) {
                totals.put(sR.getResult(), 0);
            }
            totals.put(sR.getResult(), totals.get(sR.getResult()) + 1);
        }

        return totals;
    }
}

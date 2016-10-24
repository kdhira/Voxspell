package voxspell.spell;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Date;

/**
 * Class that represents a quiz.
 * @author Kevin Hira.
 */
public class Quiz implements Serializable {
    private Date _quizDate;
    private List<Word> _quizWords;
    private LinkedList<SpellResult> _results;
    private Topic _topic;
    private Integer _nWords;
    private Boolean _isReview;

    private Boolean _isFinised;

    private Integer _wordIndex;
    private Integer _wordFlag;
    private Integer _nCorrect;


    public Quiz(Topic topic, int nWords, boolean isReview) {
        _quizDate = new Date();

        _topic = topic;
        _nWords = nWords;
        _isReview = isReview;

        _wordIndex = 0;
        _wordFlag = 0;

        _nCorrect = 0;

        _results = new LinkedList<SpellResult>();

        _quizWords = _topic.getQuizWords(nWords, _isReview);
        _isFinised = numberWords() <= 0;
    }

    /**
     * Takes a query and handles it accordingly.
     * @param query the users spelled answer.
     */
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

    }

    /**
     * Gets the number or words in the quiz.
     */
    public int numberWords() {
        return _quizWords.size();
    }

    /**
     * Gets the current word's index being asked.
     */
    public int currentIndex() {
        return _wordIndex;
    }

    /**
     * Gets the current word's index being asked.
     */
    public String currentWord() {
        return _quizWords.get(_wordIndex).toString();
    }

    /**
     * Returns the results of the quiz.
     */
    public LinkedList<SpellResult> getResults() {
        return _results;
    }

    /**
     * Checks the user inputted word against the answer.
     * @param word the users answer.
     * @return whether they match or not.
     */
    private boolean checkWord(String word) {
        return _quizWords.get(_wordIndex).toString().toLowerCase().equals(word.toLowerCase());
    }

    /**
     * Handles switching to the next word in the quiz.
     */
    private boolean nextWord() {
        WordResult wR = WordResult.parse(_wordFlag);
        if (wR == WordResult.MASTERED) {
            ++_nCorrect;
        }
        _results.addLast(new SpellResult(currentWord(), wR));
        _wordFlag = 0;
        if (++_wordIndex >= numberWords()) {
            _isFinised = true;
            return false;
        }
        return true;
    }

    /**
     * Logs the quiz results to the overall statistics.
     */
    public void logStatistics() {
        if (_quizWords.size() == _results.size()) {
            for (int i = 0; i < _quizWords.size(); ++i) {
                _quizWords.get(i).logStatistic(_results.get(i).getResult());
            }
        }
    }

    /**
     * Retrieves the total mastered, failed, faulted counts for the quiz.
     */
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

    /**
     * Gets the accuracy of the quiz.
     */
    public double getAccuracy() {
        return _wordIndex == 0 ? 0.0 : _nCorrect / (double)_wordIndex;
    }

    /**
     * Gets the completion of the quiz.
     */
    public double getCompletion() {
        return _wordIndex / (double)numberWords();
    }

    /**
     * Gets the topic this quiz belongs to.
     */
    public Topic getTopic() {
        return _topic;
    }

    /**
     * Gets the date/time this quiz was done.
     */
    public Date getDateCompleted() {
        return _quizDate;
    }
}

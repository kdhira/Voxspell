package voxspell.spell;

import java.io.Serializable;

/**
 * Represents a word, and stores the statistics about it.
 * @author Kevin Hira.
 */
public class Word implements Serializable {
    private final String _name;
    private Integer _mastered;
    private Integer _faulted;
    private Integer _failed;
    private boolean _isReview;

    public Word(String wordName) {
        _name = wordName;
        _mastered = 0;
        _faulted = 0;
        _failed = 0;
        _isReview = false;
    }

    public int getMastered() {
        return _mastered;
    }

    public int getFaulted() {
        return _faulted;
    }

    public int getFailed() {
        return _failed;
    }

    public String getName() {
        return _name;
    }

    public boolean isReview() {
        return _isReview;
    }

    public String toString() {
        return getName();
    }

    public boolean isClear() {
        return _mastered == 0 && _faulted == 0 && _failed == 0;
    }

    /**
     * Increments one of the totals based on the result.
     * @param result the result of the word attempt in a quiz.
     */
    public void logStatistic(WordResult result) {
        switch (result) {
            case MASTERED:
                ++_mastered;
                break;
            case FAULTED:
                ++_faulted;
                break;
            case FAILED:
                ++_failed;
                break;
            default:
                break;
        }
    }
}

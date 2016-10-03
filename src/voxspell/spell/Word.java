package voxspell.spell;

import java.io.Serializable;

public class Word implements Serializable {
    private String _name;
    private Integer _mastered;
    private Integer _faulted;
    private Integer _failed;
    private boolean _isReview;

    public Word(String wordName) {
        _name = wordName;
        _mastered = 0;
        _faulted = 0;
        _failed = 0;
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

    public boolean isReview() {
        return _isReview;
    }

    public String toString() {
        return _name;
    }

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

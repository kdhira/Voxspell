package voxspell.spell;

public enum WordResult {
    MASTERED("Mastered"), FAULTED("Faulted"), FAILED("Failed"), INVALID("Invalid");

    public static WordResult parse(int w) {
        switch (w) {
            case 0:
                return MASTERED;
            case 1:
                return FAULTED;
            case 2:
                return FAILED;
        }
        return INVALID;
    }

    private String _eValue;

    private WordResult(String eValue) {
        _eValue = eValue;
    }

    public String toString() {
        return _eValue;
    }
}

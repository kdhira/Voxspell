package voxspell.spell;

public enum WordResult {
    MASTERED, FAULTED, FAILED, INVALID;

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
}

package voxspell.spell;

import java.io.Serializable;

/**
 * Holds information about a word spelling in a quiz, namely what the outcome was.
 * @author Kevin Hira.
 */
public class SpellResult implements Serializable {
    private String _name;
    private WordResult _result;

    public SpellResult(String name, WordResult wordFlag) {
        _name = name;
        _result = wordFlag;
    }

    public String getName() {
        return _name;
    }

    public WordResult getResult() {
        return _result;
    }
}

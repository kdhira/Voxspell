package voxspell.spell;

public class SpellResult {
    private String _name;
    private WordResult _result;

    public SpellResult(String name, WordResult wordFlag) {
        _name = name;
        _result = wordFlag;
    }
}

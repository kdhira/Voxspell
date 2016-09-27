package voxspell.user;

import voxspell.spell.TopicSet;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;

public class User implements Serializable {
    private static User _userInstance = null;
    public static final String SAVE_DIRECTORY = "saves/";

    private String _username;
    private String _saveLocation;
    private Map<String, TopicSet> _wordlists;
    private TopicSet _targetWordlist;

    private User(String name) {
        _username = name;
        _saveLocation = SAVE_DIRECTORY + name;
        _wordlists = new HashMap<String, TopicSet>();
        _targetWordlist = null;
    }

    public static User getInstance() {
        return _userInstance;
    }

    private static void load(File userFile) {
        try (ObjectInputStream oIS = new ObjectInputStream(new FileInputStream(userFile))) {
            _userInstance = (User)oIS.readObject();
        }
        catch (IOException|ClassNotFoundException e) {

        }

        if (_userInstance == null) {
            for (TopicSet t : _userInstance._wordlists.values()) {
                t.buildTopicSet();
            }
        }

    }

    public static void switchToUser(String user) {
        File userFile = new File("saves/" + user);
        if (userFile.exists()) {
            load(userFile);
        }
        else {
            _userInstance = new User(user);
        }

        if (_userInstance == null) {
            throw new RuntimeException();
        }


    }

    public void addWordlist(String name) {
        if (!_wordlists.containsKey(name)) {
            _wordlists.put(name, new TopicSet(name));
        }
    }

    public void save() {
        File userFile = new File(_saveLocation);
        try (ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oOS.writeObject(_userInstance);
        }
        catch (IOException e) {

        }
    }
}

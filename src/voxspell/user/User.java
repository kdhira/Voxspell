package voxspell.user;

import voxspell.spell.Topic;
import voxspell.spell.TopicSet;
import voxspell.festival.Festival;

import java.io.Serializable;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

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
    private List<TopicSet> _wordlists;
    private TopicSet _targetWordlist;
    private Integer _topicPointer;

    private Integer _wordsPerQuiz;
    private String _festivalVoice;

    private User(String name) {
        _username = name;
        _saveLocation = SAVE_DIRECTORY + name;
        _wordlists = new ArrayList<TopicSet>();
        _targetWordlist = null;
        _topicPointer = -1;

        _wordsPerQuiz = 10;
        _festivalVoice = "American"; //Change to a default value, not hardcode.
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

        if (_userInstance != null) {
            for (TopicSet t : _userInstance._wordlists) {
                t.buildTopicSet();
            }
        }

    }

    public static void switchToUser(String user) {
        File userFile = new File("saves/" + user);
        if (userFile.exists()) {
            if (userFile.isFile()){
                load(userFile);
            }
            else {
                throw new RuntimeException();
            }
        }
        else {
            _userInstance = new User(user);
        }

        if (_userInstance == null) {
            throw new RuntimeException();
        }


    }

    public boolean addWordlist(String name) {
        if (find(name) == null) {
            _wordlists.add(new TopicSet(name));
            return true;
        }
        return false;
    }

    private TopicSet find(String name) {
        for (TopicSet t : _wordlists) {
            if (t.getName() == name) {
                return t;
            }
        }
        return null;
    }

    public void save() {
        File userFile = new File(_saveLocation);
        try (ObjectOutputStream oOS = new ObjectOutputStream(new FileOutputStream(userFile))) {
            oOS.writeObject(_userInstance);
        }
        catch (IOException e) {

        }
    }

    public List<TopicSet> getTopicSets() {
        return _wordlists;
    }

    public TopicSet getSelectedTopicSet() {
        return _targetWordlist;
    }

    public void setSelectedTopicSet(TopicSet t) {
        _targetWordlist = t;
    }

    public int getTopicLevel() {
        return _topicPointer;
    }

    public void setTopicLevel(int p) {
        _topicPointer = p;
    }

    public Topic targetTopic() {
        return getSelectedTopicSet().atPosition(getTopicLevel());
    }

    public String getName() {
        return _username;
    }

    public void setTopic(int topicSetIndex, int topicIndex) {
        setSelectedTopicSet(_wordlists.get(topicSetIndex));
        setTopicLevel(topicIndex);
    }

    public int getWordsPerQuiz() {
        return _wordsPerQuiz;
    }

    public void setWordsPerQuiz(int wordsPerQuiz) {
        _wordsPerQuiz = wordsPerQuiz;
    }

    public String getFestivalVoice() {
        return _festivalVoice;
    }

    public void setFestivalVoice(String voice) {
        if (!Festival.getInstance().getVoices().containsKey(voice)) {
            System.err.println(voice + " not a voice.");
            return;
        }

        _festivalVoice = voice;
    }
}

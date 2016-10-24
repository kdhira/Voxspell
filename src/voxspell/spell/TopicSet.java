package voxspell.spell;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Represents a whole word list.
 * @author Kevin Hira.
 */
public class TopicSet implements Serializable {
    private String _fileName;
    private transient List<Topic> _topicProgression;
    private Map<String, Topic> _topicPool;

    public TopicSet(String fileName) {
        _fileName = fileName;
        _topicPool = new HashMap<String, Topic>();
    }

    /**
     * Builds the topicSet from file. Everything already existing won't change, but new words can be added this way.
     * @return if the operation was successful.
     */
    public boolean buildTopicSet() {
        _topicProgression = new ArrayList<Topic>();

        Topic currentTopic = null;
        try (BufferedReader r = new BufferedReader(new FileReader(_fileName))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.startsWith("%")) {
                    addTopicToProgression(currentTopic);

                    currentTopic = getOrCreateTopic(line.substring(1));
                    if (currentTopic == null) {
                        return false;
                    }
                    continue;
                }
                else if (currentTopic == null) {
                    return false;
                }
                currentTopic.add(line);
            }

            addTopicToProgression(currentTopic);
        }
        catch (IOException e) {
            Logger.getLogger(TopicSet.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

        return true;
    }

    /**
     * Adds a topic to the progression.
     * @param t topic to add.
     */
    private void addTopicToProgression(Topic t) {
        if (t != null) {
            _topicProgression.add(t);
        }
    }

    /**
     * Gets the topic if it exists, or creates one.
     * @param topicName topic to retrieve.
     * @return the topic.
     */
    private Topic getOrCreateTopic(String topicName) {
        Topic t = null;
        if (topicName != null && topicName != "") {
            t = add(topicName);
        }
        return t;
    }

    /**
     * Add a topic to the topicSet.
     * @param topicName topic to retrieve.
     * @return the topic.
     */
    public Topic add(String topicName) {
        Topic t;
        if ((t = get(topicName)) == null) {
            _topicPool.put(topicName, t = new Topic(topicName));
        }
        return t;
    }

    /**
     * Get the topic at certain position.
     * @param i position to get from.
     * @return topic at the position.
     */
    public Topic atPosition(int i) {
        return _topicProgression.get(i);
    }

    /**
     * Gets the size of the topic progression
     * @return the size of the topic progression.
     */
    public int size() {
        return _topicProgression.size();
    }

    private Topic get(String topicName) {
        for (Topic t : _topicPool.values()) {
            if (t.getName().equals(topicName)) {
                return t;
            }
        }
        return null;
    }

    /**
     * Gets the name of the word list (the file).
     * @return the filename.
     */
    public String getName() {
        return _fileName;
    }

    /**
     * Clears the topicSet and rebuilds from scratch.
     */
    public void clear() {
        _topicProgression.clear();
        _topicPool.clear();
        buildTopicSet();

    }

}

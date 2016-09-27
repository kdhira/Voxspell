package voxspell.spell;

import java.io.Serializable;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class TopicSet implements Serializable {
    private String _fileName;
    private transient List<Topic> _topicProgression;
    private Map<String, Topic> _topicPool;

    public TopicSet(String fileName) {
        _fileName = fileName;
        _topicPool = new HashMap<String, Topic>();
        buildTopicSet();
    }

    public void buildTopicSet() {
        _topicProgression = new ArrayList<Topic>();

        Topic currentTopic = null;
        try (BufferedReader r = new BufferedReader(new FileReader(_fileName))) {
            String line;
            while ((line = r.readLine()) != null) {
                if (line.startsWith("%")) {
                    addTopicToProgression(currentTopic);

                    currentTopic = getOrCreateTopic(line.substring(1));
                    if (currentTopic == null) {
                        System.err.println("Somehow could not create topic " + line + ".");
                        return;
                    }
                    continue;
                }
                else if (currentTopic == null) {
                    System.err.println("Line \"" + line + "\" or above should be topic header, but isn't.");
                    return;
                }
                currentTopic.add(line);
            }

            addTopicToProgression(currentTopic);
        }
        catch (IOException e) {
            System.err.println("IOError.");
        }
    }

    private void addTopicToProgression(Topic t) {
        if (t != null) {
            _topicProgression.add(t);
        }
    }

    private Topic getOrCreateTopic(String topicName) {
        Topic t = null;
        if (topicName != null && topicName != "") {
            t = add(topicName);
        }
        return t;
    }

    public Topic add(String topicName) {
        Topic t;
        if ((t = get(topicName)) == null) {
            _topicPool.put(topicName, t = new Topic(topicName));
        }
        return t;
    }

    private Topic get(String topicName) {
        for (Topic t : _topicPool.values()) {
            if (t.getName().equals(topicName)) {
                return t;
            }
        }
        return null;
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package voxspell.festival;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

/**
 * Festival handles concurrently calling festival schemes to say text and change
 * voice which is used to help the user with the quiz.
 * Adapted/based on KH Assignment 2.
 * @author JC KH
 */
public class Festival {

    private static Festival _festivalInstance;
    private FestivalWorker _festivalService;

    private Map<String, String> _festivalVoices;
    private String _currentVoice;

    /**
     * The Festival constructor sets up a new Service that handles the background
     * festival subprocess and the redirection to it. It also sets up the voices
     * to be used.
     */
    private Festival() {
        _festivalInstance = null;

        _festivalVoices = new HashMap<String, String>();
        _festivalVoices.put("American", "voice_kal_diphone");
        _festivalVoices.put("New Zealand", "voice_akl_nz_jdt_diphone");

        _currentVoice = "American";
    }

    /**
     * The FestivalWorker class handles the background management of the festival
     * subprocess.
     */
    private class FestivalWorker extends Service<Void> {
        Process _process = null;
        String _nextText = "";

        /**
         * The FestivalWorker constructor sets up the festival subprocess.
         */
        public FestivalWorker() {
            ProcessBuilder pb = new ProcessBuilder("bash", "-c", "festival");
            try {
                _process = pb.start();
            } catch (IOException ex) {
                Logger.getLogger(Festival.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        /**
         * Sets the text (a scheme) which should be added and redirected
         * into festival next.
         * @param scheme The scheme to add to festival.
         */
        public void setText(String scheme) {
            _nextText = scheme;
        }

        /**
         * Generates a new background task to add schemes to festival.
         * @return
         */
        @Override
        protected Task<Void> createTask() {
            final String text = _nextText;
            return new Task<Void>() {
                protected Void call() throws Exception {
                    doWork(_process.getOutputStream(), text);
                    return null;
                }
            };
        }

        /**
         * Opens the output stream to festival and writes through the scheme to
         * be added.
         * @param stream The output stream to the festival subprocess.
         * @param text The scheme to add.
         * @throws IOException
         */
        private synchronized void doWork(OutputStream stream, String text) throws IOException {
            BufferedWriter w = new BufferedWriter(new OutputStreamWriter(stream));
            w.write(text);
            w.newLine();
            //System.out.println(text);
            w.flush();
        }

    }

    /**
     * Gets the instance of the Festival singleton.
     * @return The instance of the Festival singleton.
     */
    public static Festival getInstance() {
        if (_festivalInstance == null) {
            _festivalInstance = new Festival();
        }
        return _festivalInstance;
    }

    /**
     * Wraps text into a scheme and sends it to the FestivalWorker.
     * @param text The text to sent.
     */
    public synchronized void speak(String text) {
        System.out.println("(SayText \"" + text + "\")");
        _festivalService.setText("(SayText \"" + text + "\")");
        new Thread(_festivalService.createTask()).start();

    }

    /**
     * Wraps voice text into scheme and sends it to the FestivalWorker.
     */
    public void changeVoice() {
        System.out.println("("+_festivalVoices.get(_currentVoice)+")");
        _festivalService.setText("("+_festivalVoices.get(_currentVoice)+")");
        new Thread(_festivalService.createTask()).start();
    }

    public void setVoice(String voice) {
        if (!_festivalVoices.containsKey(voice)) {
            System.err.println(voice + " not a voice.");
            return;
        }
        _currentVoice = voice;
    }

    public void changeStretch(String stretch) {
        System.out.println("(Parameter.set 'Duration_Stretch " + stretch + ")");
        _festivalService.setText("(Parameter.set 'Duration_Stretch " + stretch + ")");
        new Thread(_festivalService.createTask()).start();
    }

    /**
     * Retrieves the voices for festival.
     * @return A map of the voices that can be used in festival.
     */
    public Map<String, String> getVoices() {
        return _festivalVoices;
    }

    /**
     * Ends the festival subprocess.
     */
    public void closeFestival() {
        if (_festivalService != null && _festivalService._process != null) {
            _festivalService._process.destroy();
            System.out.println("Festival subprocess destroyed.");
        }
        _festivalService = null;
    }

    /**
     * Starts a festival subprocess.
     */
    public void openFestival() {
        closeFestival();
        _festivalService = new FestivalWorker();
        System.out.println("Festival subprocess spawned.");
    }


}

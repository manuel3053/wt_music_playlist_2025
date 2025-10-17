package it.polimi.tiw.wt_music_playlist_2025.controller;

public class MissingSessionAttribute extends Exception {
    public MissingSessionAttribute(String message) {
        super("Missing session attribute: " + message);
    }
}

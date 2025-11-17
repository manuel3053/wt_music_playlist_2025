package it.polimi.tiw.wt_music_playlist_2025.controller;

public class InvalidFileType extends Exception {
    public InvalidFileType(String type) {
        super("The file must be of type: " + type);
    }
}

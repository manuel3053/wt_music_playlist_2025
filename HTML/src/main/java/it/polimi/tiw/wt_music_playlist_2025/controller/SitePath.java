package it.polimi.tiw.wt_music_playlist_2025.controller;

public enum SitePath {
    HOME("home"),
    PLAYLIST("playlist"),
    TRACK("track"),
    LOGIN("login"),
    SUBSCRIBE("subscribe");

    public String go() {
        return "redirect:" + path + "/view";
    }

    public String reload() {
        return "redirect:view";
    }

    public String show() {
        return path;
    }

    private final String path;
    SitePath(String path) {
        this.path = path;
    }
}

package it.polimi.tiw.wt_music_playlist_2025.controller;

public enum Route {
    HOME("home"),
    PLAYLIST("playlist"),
    TRACK("track"),
    LOGIN("login"),
    SUBSCRIBE("subscribe");

    public String go() {
        return "redirect:/" + path + "/view";
    }

    public String goWithIndex(int index) {
        return "redirect:/" + path + "/view" + index;
    }

    public String reload() {
        return "redirect:view";
    }

    public String show() {
        return path;
    }

    private final String path;
    Route(String path) {
        this.path = path;
    }
}

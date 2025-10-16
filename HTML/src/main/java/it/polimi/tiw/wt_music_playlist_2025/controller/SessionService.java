package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpSession;

public class SessionService {
    static private String playlistIdKey = "PLAYLIST_ID";
    static private String trackIdKey = "TRACK_ID";
    static private String userKey = "USER_KEY";

    private SessionService() {}

    static int getSelectedPlaylistId(HttpSession session) {
        return (int) session.getAttribute(playlistIdKey);
    }

    static int getSelectedTrackId(HttpSession session) {
        return (int) session.getAttribute(trackIdKey);
    }

    static User getUser(HttpSession session) {
        return (User) session.getAttribute(userKey);
    }

    static void setSelectedPlaylistId(HttpSession session, int id) {
        session.setAttribute(playlistIdKey, id);
    }

    static void setSelectedTrackId(HttpSession session, int id) {
        session.setAttribute(trackIdKey, id);
    }

    static void setUser(HttpSession session, User user) {
        session.setAttribute(userKey, user);
    }

}

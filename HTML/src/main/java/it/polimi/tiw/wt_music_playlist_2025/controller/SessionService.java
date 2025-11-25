package it.polimi.tiw.wt_music_playlist_2025.controller;

import it.polimi.tiw.wt_music_playlist_2025.entity.User;
import jakarta.servlet.http.HttpSession;

public class SessionService {
    static private String playlistIdKey = "PLAYLIST_ID";
    static private String trackIdKey = "TRACK_ID";
    static private String userKey = "USER_KEY";

    private SessionService() {}

    static boolean checkValidAccess(HttpSession session, int userId) {
        try {
            if (userId != getUser(session).getId()) {
                return false;
            }
        } catch (MissingSessionAttribute e) {
            return false;
        }
        return true;
    }

    static Integer getSelectedPlaylistId(HttpSession session) throws MissingSessionAttribute {
        Integer id = (Integer) session.getAttribute(playlistIdKey);
        if (id == null) {
            throw new MissingSessionAttribute("Missing playlist id");
        }
        return id;
    }

    static Integer getSelectedTrackId(HttpSession session) throws MissingSessionAttribute {
        Integer id = (Integer) session.getAttribute(trackIdKey);
        if (id == null) {
            throw new MissingSessionAttribute("Missing playlist id");
        }
        return id;
    }

    static User getUser(HttpSession session) throws  MissingSessionAttribute {
        User user = (User) session.getAttribute(userKey);
        if (user == null) {
            throw new MissingSessionAttribute("Missing user attribute");
        }
        return user;
    }

    static void setSelectedPlaylistId(HttpSession session, Integer id) {
        session.setAttribute(playlistIdKey, id);
    }

    static void setSelectedTrackId(HttpSession session, Integer id) {
        session.setAttribute(trackIdKey, id);
    }

    static void setUser(HttpSession session, User user) {
        session.setAttribute(userKey, user);
    }

}

package it.polimi.tiw.wt_music_playlist_2025.controller;

import org.springframework.security.core.context.SecurityContextHolder;

public class UserDetailsExtractor {

    private UserDetailsExtractor() {

    }

    public static int getUserId() {
        return ((UserWithId) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getId();
    }

}

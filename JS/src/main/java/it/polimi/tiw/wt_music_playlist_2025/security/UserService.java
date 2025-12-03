package it.polimi.tiw.wt_music_playlist_2025.security;

import it.polimi.tiw.wt_music_playlist_2025.DAO.UserDAO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserService implements UserDetailsService {

    private final UserDAO userDAO;

    public UserService(UserDAO userDao) {
        this.userDAO = userDao;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        it.polimi.tiw.wt_music_playlist_2025.entity.User user = userDAO.findByUsername(username);
        if (user != null) {
            return new UserWithId(user.getId(), user.getUsername(), "{noop}"+user.getPassword(), new ArrayList<>());
        }
        throw new UsernameNotFoundException(username);
    }

}

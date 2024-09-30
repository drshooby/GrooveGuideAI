package music_app.music_app_backend.service;

import jakarta.persistence.EntityNotFoundException;
import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AppUserService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    private String loggedUsername;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = appUserRepository.findByUserName(username);
        if (user.isPresent()) {
            AppUser au = user.get();
            loggedUsername = au.getUserName();
            System.out.println("User \"" + loggedUsername + "\" logged in.");
            return User.builder()
                    .username(au.getUserName())
                    .password(au.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }

    public String getLoggedUsername() {
        return loggedUsername;
    }

    public Long findIdByUserName(String username) {
        AppUser appUser = appUserRepository.findByUserName(username).orElseThrow(() ->
                new EntityNotFoundException(username + " doesn't exist,"));
        return appUser.getId();
    }
}

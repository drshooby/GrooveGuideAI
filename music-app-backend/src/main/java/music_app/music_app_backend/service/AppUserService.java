package music_app.music_app_backend.service;

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
    private AppUserRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = repository.findByUserName(username);
        if (user.isPresent()) {
            AppUser au = user.get();
            return User.builder()
                    .username(au.getUserName())
                    .password(au.getPassword())
                    .build();
        } else {
            throw new UsernameNotFoundException(username);
        }
    }
}
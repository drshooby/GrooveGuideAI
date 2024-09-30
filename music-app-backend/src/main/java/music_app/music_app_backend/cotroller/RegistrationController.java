package music_app.music_app_backend.cotroller;

import music_app.music_app_backend.DTO.AppUserDTO;
import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegistrationController {

    @Autowired
    private AppUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public String createUser(@RequestBody AppUserDTO user) {
        System.out.println(user);
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(new AppUser(user.getUserName(), user.getPassword()));
        return "redirect:/req/login";
    }
}

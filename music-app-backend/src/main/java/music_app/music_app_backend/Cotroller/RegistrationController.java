package music_app.music_app_backend.Cotroller;

import music_app.music_app_backend.DTO.AppUserDTO;
import music_app.music_app_backend.Entity.AppUser;
import music_app.music_app_backend.Repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class RegistrationController {

    @Autowired
    private AppUserRepository repository;

    @Autowired
    private PasswordEncoder encoder;

    @PostMapping(value = "/req/signup", consumes = "application/json")
    public ResponseEntity<Map<String, String>> createUser(@RequestBody AppUserDTO user) {
        System.out.println(user);
        user.setPassword(encoder.encode(user.getPassword()));
        repository.save(new AppUser(user.getUserName(), user.getPassword()));
        Map<String, String> rsp = new HashMap<>();
        rsp.put("messageContent", "User registered successfully");
        rsp.put("successStatus", "ok");
        return ResponseEntity.ok(rsp);
    }
}

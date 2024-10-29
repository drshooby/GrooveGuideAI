package music_app.music_app_backend.Cotroller;

import music_app.music_app_backend.Service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.Map;

@Controller
public class FriendshipController {

    @Autowired
    private AppUserService userService;

    @PostMapping(value = "/api/friendship", consumes = "application/json")
    public ResponseEntity<Map<String, String>> addNewFriend(@RequestBody Map<String, String> body) {

        Map<String, String> rsp = new HashMap<>();

        String friendName = body.get("friendName");
        String currUser = userService.getLoggedUsername();

        // TODO! try to create the friendship, if successful return a Map like so { statusMessage: "You are now friends with user *username*" }

        return ResponseEntity.ok(rsp); // placeholder, replace this
    }
}

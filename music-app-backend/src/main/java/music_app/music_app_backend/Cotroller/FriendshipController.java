package music_app.music_app_backend.Cotroller;

import music_app.music_app_backend.Service.AppUserService;
import music_app.music_app_backend.Service.FriendshipService;
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
    @Autowired
    private FriendshipService friendshipService;

    @PostMapping(value = "/api/friendship", consumes = "application/json")
    public ResponseEntity<Map<String, String>> addNewFriend(@RequestBody Map<String, String> body) {
        Map<String, String> rsp = new HashMap<>();

        String currUser = userService.getLoggedUsername();
        String friendName = body.get("friendName");

        String success = String.format("You are now friends with %s", friendName);
        if (friendshipService.addFriendship(currUser, friendName)) {
            rsp.put("resultMessage", success);
        } else {
            rsp.put("resultMessage", "Failed to add friend");
        }
        return ResponseEntity.ok(rsp); // placeholder, replace this
    }
}

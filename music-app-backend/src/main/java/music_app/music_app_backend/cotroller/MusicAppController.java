package music_app.music_app_backend.cotroller;

import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.service.AppUserService;
import music_app.music_app_backend.service.LLMService;
import music_app.music_app_backend.service.SongService;
import music_app.music_app_backend.service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MusicAppController {
    private final LLMService llmService;
    private final SongService songService;
    private final AppUserService userService;
    private final UserFavoriteService userFavoriteService;

    @Autowired
    public MusicAppController(LLMService llmService, SongService songService, AppUserService userService, UserFavoriteService userFavoriteService) {
        this.llmService = llmService;
        this.songService = songService;
        this.userService = userService;
        this.userFavoriteService = userFavoriteService;
    }

    /**
     * User makes their song or genre selection with input on the front-end, this function receives it
     * @param body format {searchType=*artist or genre*, input=*user text input*}
     */

    @PostMapping(value = "/api/recommend", consumes = "application/json")
    public ResponseEntity<Map<String, String>> handleUserChoice(@RequestBody Map<String, String> body) {
        System.out.println("POST received @ /recommend/api");
        String searchType = body.get("searchType");
        String input = body.get("input");
        Map<String, String> rsp = buildResponse(searchType, input);
        return ResponseEntity.ok(rsp);
    }

    private Map<String, String> buildResponse(String searchType, String input) {
        Map<String, String> rsp = new HashMap<>();
        // TODO! Call LLM function to get requests and store them in the map using the following format:
        // {"song1": "song", "song2": "song", "song3", song}
        // the songs we send to the front-end are just strings btw, objects are for the backend
        return rsp;
    }

//    @GetMapping("/recommend")
//    public void test() {
//        String input = "Hikaru Utada";
//        List<SongDTO> recommendations = llmService.recommend(input);
//        for (SongDTO song : recommendations) {
//            System.out.println(song);
//        }
//    }
//
//    @GetMapping("/addSong")
//    public String addSong() {
//        SongDTO newSong = new SongDTO("Test Song", "David");
//        songService.insertNewSong(newSong);
//        return "Song added to db";
//    }

//    @GetMapping("/inputUserName")
//    public void inputUsernameTest() {
//        String userName = "Koichi1212";
//        String input = "Ed Sheeran";
//        String password = "";
//        UserDTO userDTO = userService.findUserByUserName(userName, password);
//        List<SongDTO> favoriteSongs;
//        favoriteSongs = userFavoriteService.getUserFavoriteSongs(userDTO.getId());
//        List<SongDTO> recommendations;
//        if (favoriteSongs.isEmpty()) {
//            recommendations = llmService.recommend(input);
//        } else {
//            recommendations = llmService.recommend(input, favoriteSongs);
//        }
//        for (SongDTO song : recommendations) {
//            System.out.println(song);
//        }
//    }
//
//    @GetMapping("/inputUserNames/{userName}")
//    public ResponseEntity<String> inputUsernameTest2(@PathVariable String userName) {
//        String input = "Ed Sheeran"; // dao
//        UserDTO userDTO = userService.findUserByUserName(userName, "");
//        List<SongDTO> favoriteSongs;
//        favoriteSongs = userFavoriteService.getUserFavoriteSongs(userDTO.getId());
//        List<SongDTO> recommendations;
//        if (favoriteSongs.isEmpty()) {
//            recommendations = llmService.recommend(input);
//        } else {
//            recommendations = llmService.recommend(input, favoriteSongs);
//        }
//        for (SongDTO song : recommendations) {
//            System.out.println(song);
//        }
//
//        return ResponseEntity.ok(userName);
//    }


}

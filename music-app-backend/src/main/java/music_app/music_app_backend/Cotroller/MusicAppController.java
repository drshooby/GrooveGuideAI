package music_app.music_app_backend.Cotroller;

import music_app.music_app_backend.Service.AppUserService;
import music_app.music_app_backend.Service.LLMService;
import music_app.music_app_backend.Service.SongService;
import music_app.music_app_backend.Service.UserFavoriteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NameNotFoundException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MusicAppController {
    private final LLMService llmService;
    private final SongService songService;
    private final AppUserService userService;
    private final UserFavoriteService userFavoriteService;

    private Map<Integer, String> songNames = new HashMap<>();


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
    public ResponseEntity<Map<String, String>> handleUserSearch(@RequestBody Map<String, String> body) {
        System.out.println("POST received @ /recommend/api");
        String input = body.get("input");
        Map<String, String> rsp = buildResponse(input);
        if (!isValidResponse(rsp)) {
            return ResponseEntity
                    .badRequest()
                    .body(new HashMap<>());
        }
        return ResponseEntity.ok(rsp);
    }

    private boolean isValidResponse(Map<String, String> rsp) {
        return rsp.size() == 3 && rsp.containsKey("song1")
                && rsp.containsKey("song2") && rsp.containsKey("song3");
    }

    private Map<String, String> buildResponse(String input) {
        String currUser = userService.getLoggedUsername();
        Map<String, String> rsp = new HashMap<>();

        Long userId = userService.findIdByUserName(currUser);
        String favoriteSongs = userFavoriteService.getFavoriteSongsByUserId(userId);

        List<String> songs;
        if (favoriteSongs != null) {
            System.out.println(currUser + " liked " + favoriteSongs + " in the past.");
            songs = llmService.recommend(input, favoriteSongs);

        } else {
            System.out.println("No favorite songs saved in the database for " + currUser + ".");
            songs = llmService.recommend(input);
            //songs = llmService.recommendByPOJO(input);
        }
        for (int i = 1; i <= 3; i++) {
            this.songNames.put(i, songs.get(i - 1));
            rsp.put("song" + i, songs.get(i - 1));
            System.out.println(songs.get(i - 1));
        }
        return rsp;
    }

    @PostMapping(value = "/api/liked", consumes = "application/json")
    public ResponseEntity<Void> handleUserLikes(@RequestBody Map<Integer, Boolean> body) {
        System.out.println("POST received @ /liked/api");
        handleLikesAndDisliked(body);
        return ResponseEntity.ok().build();
    }

    private void handleLikesAndDisliked(Map<Integer, Boolean> songNumToLiked) {
        for (Map.Entry<Integer, Boolean> reaction : songNumToLiked.entrySet()) {
            if (reaction.getValue()) { // If true i.e. if the user liked
                String songByArtist = songNames.get(reaction.getKey());
                String[] songList = createSongListFromString(songByArtist);

                songService.insertNewSong(songList[0], songList[1]);

                saveUserFavorite(songList[0], songList[1]);

                System.out.println(songByArtist + " was saved in database for " + userService.getLoggedUsername());
            }
        }
    }

    private String[] createSongListFromString(String songByArtist) {
        String[] songList = songByArtist.split(" by ");
        songList[0] = songList[0].replace("\"", "").trim();
        songList[1] = songList[1].replace("\"", "").trim();
        return songList;
    }

    private void saveUserFavorite(String songName, String artistName) {
        try {
            Long userId = userService.findIdByUserName(userService.getLoggedUsername());
            Long songId = songService.findIdBySongNameAndArtistName(songName, artistName);

            userFavoriteService.addFavorite(userId, songId);
        } catch (NameNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}

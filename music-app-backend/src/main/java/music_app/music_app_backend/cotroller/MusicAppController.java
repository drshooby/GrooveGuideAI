package music_app.music_app_backend.cotroller;

import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.entity.Song;
import music_app.music_app_backend.entity.UserFavorite;
import music_app.music_app_backend.service.AppUserService;
import music_app.music_app_backend.service.LLMService;
import music_app.music_app_backend.service.SongService;
import music_app.music_app_backend.service.UserFavoriteService;
import org.apache.catalina.User;
import org.springframework.aop.scope.ScopedProxyUtils;
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
        String searchType = body.get("searchType");
        String input = body.get("input");
        Map<String, String> rsp = buildResponse(searchType, input);
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

    private Map<String, String> buildResponse(String searchType, String input) {
        String currUser = userService.getLoggedUsername();
        Map<String, String> rsp = new HashMap<>();
        // TODO! Call LLM function to get requests and store them in the map using the following format:
        // {"song1": "song", "song2": "song", "song3", song}, the for loop updates everything
        // the songs we send to the front-end are just strings btw, objects are for the backend
        // song names are in an instance map of this class for access across functions, keys are 1-3
        // example:

        Long userId = userService.findIdByUserName(userService.getLoggedUsername());
        String favoriteSongs = userFavoriteService.getFavoriteSongsByUserId(userId);

        List<String> songs;
        if (favoriteSongs != null) {
            System.out.println(userService.getLoggedUsername() + " liked " + favoriteSongs + " in the past.");

            songs = llmService.recommend(input);

        } else {
            System.out.println("No favorite songs saved in the database for " + userService.getLoggedUsername() + ".");

            songs = llmService.recommend(input, favoriteSongs);
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
        // TODO! fill this out
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

package music_app.music_app_backend.Cotroller;

import music_app.music_app_backend.Service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NameNotFoundException;
import java.util.*;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
public class MusicAppController {
    @Autowired
    private LLMService llmService;
    @Autowired
    private SongService songService;
    @Autowired
    private AppUserService userService;
    @Autowired
    private UserFavoriteService userFavoriteService;
    @Autowired
    private FriendshipService friendshipService;
    @Autowired
    private DeezerService deezerService;

    private final Map<Integer, String> songNames = new HashMap<>();


    /**
     * User makes their song or genre selection with input on the front-end, this function receives it
     * @param body format {searchType=*artist or genre*, input=*user text input*}
     */
    @PostMapping(value = "/api/recommend", consumes = "application/json")
    public ResponseEntity<Map<String, String[]>> handleUserSearch(@RequestBody Map<String, String> body) {
        System.out.println("POST received @ /recommend/api");
        String searchType = body.get("searchType");
        String input = body.get("input");
        System.out.println(input);
        Map<String, String[]> rsp = buildResponse(input, searchType);
        if (!isValidResponse(rsp)) {
            return ResponseEntity
                    .badRequest()
                    .body(new HashMap<>());
        }
        return ResponseEntity.ok(rsp);
    }

    private boolean isValidResponse(Map<String, String[]> rsp) {
        return rsp.size() == 3 && rsp.containsKey("song1")
                && rsp.containsKey("song2") && rsp.containsKey("song3");
    }

    private Map<String, String[]> buildResponse(String input, String searchType) {
        Map<String, String[]> rsp = new HashMap<>();

        String currUser = userService.getLoggedUsername();
        Long userId = userService.findIdByUserName(currUser);


        StringBuilder favoriteSongs = new StringBuilder();
        List<String> songs;
        switch (searchType) {
            case "input-only":
                songs = llmService.recommend(input);
                break;
            case "memory":
                favoriteSongs.append(userFavoriteService.getFavoriteSongsByUserId(userId));
                System.out.println("User " + currUser + " liked these songs before: " + favoriteSongs);
                songs = (favoriteSongs.isEmpty()) ? llmService.recommend(input)
                        : llmService.recommend(input, favoriteSongs.toString());
                break;
            case "friend":
                List<Long> friends = friendshipService.getFriendsByUserId(userId);
                for (Long friendId : friends) {
                    favoriteSongs.append(userFavoriteService.getFavoriteSongsByUserId(friendId)).append(", ");
                }
                System.out.println("User " + currUser + "'s friends liked these songs before: " + favoriteSongs);
                songs = (favoriteSongs.isEmpty()) ? llmService.recommend(input)
                        : llmService.recommend(input, favoriteSongs.toString());
                break;
            case "both":
                favoriteSongs.append(userFavoriteService.getFavoriteSongsByUserId(userId)).append(", ");
                System.out.println("User " + currUser + " liked these songs before: " + favoriteSongs);
                List<Long> friendIds = friendshipService.getFriendsByUserId(userId);
                for (Long friendId : friendIds) {
                    favoriteSongs.append(userFavoriteService.getFavoriteSongsByUserId(friendId)).append(", ");
                }
                System.out.println("User " + currUser + "'s friends liked these songs before: " + favoriteSongs);
                songs = (favoriteSongs.isEmpty()) ? llmService.recommend(input)
                        : llmService.recommend(input, favoriteSongs.toString());
                break;
            default:
                return rsp;
        }

        for (String song : songs) {
            if (!song.contains("by")) {
                return rsp;
            }
        }

        for (int i = 1; i <= 3; i++) {
            String currSong = songs.get(i - 1);
            songNames.put(i, currSong);
            String[] songAndTrack = currSong.split("by");
            String[] albumCoverAndLink = deezerService.getAlbumImageURL(songAndTrack[1].strip(), songAndTrack[0].strip());
            String[] output = albumCoverAndLink.length == 2 ?
                    new String[]{currSong, albumCoverAndLink[0], albumCoverAndLink[1]} :
                    new String[]{currSong, "", ""};
            rsp.put("song" + i, output);
            System.out.println(currSong);
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

    private String findUsersFavoriteSongs(Long userId) {
        return userFavoriteService.getFavoriteSongsByUserId(userId);
    }

    private List<Long> findFriends(Long userId) {
        return friendshipService.getFriendsByUserId(userId);
    }

    private String findFriendsFavoriteSongs(Long userId) {
        List<Long> friends = findFriends(userId);
        if (friends != null) {
           StringBuilder sb = new StringBuilder();
           for (Long friendId : friends) {
               sb.append(findUsersFavoriteSongs(friendId));
           }
           return sb.toString();
        }
        return null;
    }
}

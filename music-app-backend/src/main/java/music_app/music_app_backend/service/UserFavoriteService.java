package music_app.music_app_backend.service;

import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.entity.Song;
import music_app.music_app_backend.entity.UserFavorite;
import music_app.music_app_backend.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserFavoriteService {
    private final UserFavoriteRepository userFavoriteRepository;

    @Autowired
    public UserFavoriteService(UserFavoriteRepository userFavoriteRepository, AppUserService userService, SongService songService) {
        this.userFavoriteRepository = userFavoriteRepository;
    }

    @Transactional
    public void addFavorite(Long userId, Long songId) {
        UserFavorite userFavorite = new UserFavorite();
        userFavorite.setUser(new AppUser(userId));
        userFavorite.setSong(new Song(songId));
        userFavoriteRepository.save(userFavorite);
    }

    public String getFavoriteSongsByUserId(Long userId) {
        if (!userFavoriteRepository.existsByUserId(userId)) {
            System.out.println("This user's favorite songs are not in the database.");
            return null;
        }

        List<String> favoriteSongs = userFavoriteRepository.findByUserId(userId)
                .stream()
                .map(UserFavorite::getSongInString)
                .collect(Collectors.toList());

        return String.join(", ", favoriteSongs);
    }

}

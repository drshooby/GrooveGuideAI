package music_app.music_app_backend.service;

import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.entity.Song;
import music_app.music_app_backend.entity.UserFavorite;
import music_app.music_app_backend.repository.UserFavoriteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    public List<SongDTO> getUserFavoriteSongs(Long userId) {
//        return userFavoriteRepository.findFavoriteSongsByUserId(userId);
//    }
}

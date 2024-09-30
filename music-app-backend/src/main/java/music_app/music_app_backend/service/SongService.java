package music_app.music_app_backend.service;

import jakarta.persistence.EntityNotFoundException;
import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.entity.AppUser;
import music_app.music_app_backend.entity.Song;
import music_app.music_app_backend.repository.SongRepository;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;
import java.util.Optional;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public void insertNewSong(String songName, String artistName) {
        if (!songRepository.existsBySongNameAndArtistName(songName, artistName)) {
            songRepository.save(new Song(songName, artistName));
            System.out.println(songName + " by " + artistName + " was saved in database.");
            return;
        }
        System.out.println(songName + " by " + artistName + " is already in database.");
    }

    public Long findIdBySongName(String songName) {
        return songRepository.findIdBySongName(songName);
    }

    public Long findIdBySongNameAndArtistName(String songName, String artistName) throws NameNotFoundException {
        Song song = songRepository.findBySongNameAndArtistName(songName, artistName)
                .orElseThrow(() -> new EntityNotFoundException(
                        songName + " by "  + artistName + " doesn't exist,"));
        return song.getId();
    }
}

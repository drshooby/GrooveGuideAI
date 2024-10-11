package music_app.music_app_backend.Service;

import jakarta.persistence.EntityNotFoundException;
import music_app.music_app_backend.Entity.Song;
import music_app.music_app_backend.Repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.naming.NameNotFoundException;

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

    public Long findIdBySongNameAndArtistName(String songName, String artistName) throws NameNotFoundException {
        Song song = songRepository.findBySongNameAndArtistName(songName, artistName)
                .orElseThrow(() -> new EntityNotFoundException(
                        songName + " by "  + artistName + " doesn't exist,"));
        return song.getId();
    }
}

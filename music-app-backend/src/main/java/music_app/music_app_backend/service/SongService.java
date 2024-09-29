package music_app.music_app_backend.service;

import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.entity.Song;
import music_app.music_app_backend.repository.SongRepository;
import org.springframework.aop.scope.ScopedProxyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SongService {
    private final SongRepository songRepository;

    @Autowired
    public SongService(SongRepository songRepository) {
        this.songRepository = songRepository;
    }

    public void insertNewSong(SongDTO songDTO) {
        if (!songRepository.existsBySongName(songDTO.getSongName())) {
            songRepository.save(new Song(songDTO.getSongName(), songDTO.getArtistName()));
            System.out.println("Attempted to save " + songDTO.getSongName() + ", and it is saved in database.");
            return;
        }
        System.out.println("Attempted to save " + songDTO.getSongName() + ", but it is already in database");
    }

    public Long findIdBySongName(String songName) {
        return songRepository.findIdBySongName(songName);
    }
}

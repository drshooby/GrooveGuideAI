package music_app.music_app_backend.Repository;

import music_app.music_app_backend.Entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Long findIdBySongName(String songName);

    boolean existsBySongNameAndArtistName(String songName, String artistName);

    Optional<Song> findBySongNameAndArtistName(String songName, String artistName);
}

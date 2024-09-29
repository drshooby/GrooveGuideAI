package music_app.music_app_backend.repository;

import music_app.music_app_backend.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {

    Long findIdBySongName(String songName);

    boolean existsBySongName(String songName);
}

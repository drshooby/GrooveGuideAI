package music_app.music_app_backend.Repository;

import music_app.music_app_backend.Entity.UserFavorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserFavoriteRepository extends JpaRepository<UserFavorite, Long> {
    // Returns empty list, not null when no UserFavorite entities found
    List<UserFavorite> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}

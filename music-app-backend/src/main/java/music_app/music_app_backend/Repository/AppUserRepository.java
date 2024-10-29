package music_app.music_app_backend.Repository;

import music_app.music_app_backend.Entity.AppUser;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUserName(String username);

    Optional<AppUser> findById(Long userId);

    Long findIdByUserName(String username);
}

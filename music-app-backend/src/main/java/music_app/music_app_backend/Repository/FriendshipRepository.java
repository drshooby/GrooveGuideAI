package music_app.music_app_backend.Repository;

import music_app.music_app_backend.Entity.AppUser;
import music_app.music_app_backend.Entity.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    // Returns empty list, not null when no Friendship entities found
    List<Friendship> findByUser1Id(Long userId);

    boolean existsByUser1Id(Long user1Id);

    boolean existsByUser1AndUser2(AppUser user1, AppUser user2);
}

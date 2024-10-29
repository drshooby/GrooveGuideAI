package music_app.music_app_backend.Service;

import music_app.music_app_backend.Entity.AppUser;
import music_app.music_app_backend.Entity.Friendship;
import music_app.music_app_backend.Repository.AppUserRepository;
import music_app.music_app_backend.Repository.FriendshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private AppUserRepository appUserRepository;
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Transactional
    public void addFriendship(Long user1Id, Long user2Id) {
        AppUser user1 = appUserRepository.findById(user1Id)
                .orElseThrow(() -> new IllegalArgumentException("User " + user1Id + " not found."));
        AppUser user2 = appUserRepository.findById(user2Id)
                .orElseThrow(() -> new IllegalArgumentException("User " + user2Id + " not found."));

        if (friendshipRepository.existsByUser1AndUser2(user1, user2) ||
                friendshipRepository.existsByUser1AndUser2(user2, user1)) {
            System.out.println("User " + user1.getUserName() + " and User " + user2.getUserName()
                    + " are already friends.");
            return;
        }

        Friendship friendship1 = new Friendship(user1, user2);
        Friendship friendship2 = new Friendship(user2, user1);
        friendshipRepository.save(friendship1);
        friendshipRepository.save(friendship2);
    }

    public List<Long> getFriendsByUserId(Long userId) {
        if (!friendshipRepository.existsByUser1Id(userId)) {
            System.out.println("This user doesn't have any friends yet.");
            return null;
        }

        List<Long> friends = friendshipRepository.findByUser1Id(userId)
                .stream()
                .map(Friendship::getUser2Id)
                .collect(Collectors.toList());

        return friends;
    }
}

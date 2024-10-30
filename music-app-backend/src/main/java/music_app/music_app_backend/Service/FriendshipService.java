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

    // user1 wants to follow user2
    @Transactional
    public boolean addFriendship(String user1Name, String user2Name) {
        AppUser user1 = appUserRepository.findByUserName(user1Name)
                .orElseThrow(() -> new IllegalArgumentException("User " + user1Name + " not found."));
        AppUser user2 = appUserRepository.findByUserName(user2Name)
                .orElseThrow(() -> new IllegalArgumentException("User " + user2Name + " not found."));

        if (friendshipRepository.existsByUser1AndUser2(user1, user2)) {
            System.out.println("User " + user1.getUserName() + " and User " + user2.getUserName()
                    + " are already friends.");
            return false;
        }

        Friendship friendship1 = new Friendship(user1, user2);
        friendshipRepository.save(friendship1);
        return true;
    }

    // Find users that the user1 (current user) is following i.e. user2
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

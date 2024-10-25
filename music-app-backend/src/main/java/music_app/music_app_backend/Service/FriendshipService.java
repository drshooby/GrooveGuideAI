package music_app.music_app_backend.Service;

import music_app.music_app_backend.Entity.AppUser;
import music_app.music_app_backend.Entity.Friendship;
import music_app.music_app_backend.Repository.FriendshipRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    @Autowired
    private FriendshipRepository friendshipRepository;

    @Transactional
    public void addFriendship(Long user1Id, Long user2Id) {
        Friendship friendship1 = new Friendship(
                new AppUser(user1Id), new AppUser(user2Id));
        Friendship friendship2 = new Friendship(
                new AppUser(user2Id), new AppUser(user1Id));
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

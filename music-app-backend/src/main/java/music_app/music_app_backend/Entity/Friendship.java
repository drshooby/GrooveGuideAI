package music_app.music_app_backend.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "friendship")
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_1", nullable = false)
    private AppUser user1;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id_2", nullable = false)
    private AppUser user2;

    public Friendship() {}
    public Friendship(AppUser user1, AppUser user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    public Long getUser2Id() {
        return user2.getId();
    }

    public String getFriendshipInString() {
        StringBuilder sb = new StringBuilder();
        sb.append(user1.getUserName());
        sb.append(" is a friend of ");
        sb.append(user2.getUserName());
        return sb.toString();
    }
}

package music_app.music_app_backend.entity;


import jakarta.persistence.*;

@Entity
@Table(name = "user_favorites")
public class UserFavorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "song_id", nullable = false)
    private Song song;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AppUser getUser() {
        return user;
    }

    public void setUser(AppUser user) {
        this.user = user;
    }

    public String getSongInString() {
        StringBuilder sb = new StringBuilder();
        sb.append(song.getSongName());
        sb.append(" by ");
        sb.append(song.getArtistName());
        return sb.toString();
    }

    public void setSong(Song song) {
        this.song = song;
    }
}

package music_app.music_app_backend.Entity;


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

    public UserFavorite() {}
    public UserFavorite(AppUser appUser, Song song) {
        this.user = appUser;
        this.song = song;
    }

    public String getSongInString() {
        StringBuilder sb = new StringBuilder();
        sb.append(song.getSongName());
        sb.append(" by ");
        sb.append(song.getArtistName());
        return sb.toString();
    }
}

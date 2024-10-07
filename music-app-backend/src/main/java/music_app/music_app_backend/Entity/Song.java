package music_app.music_app_backend.Entity;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.Set;

@Entity
@Table(name = "songs")
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String songName;

    @Column(nullable = false)
    private String artistName;

    @OneToMany(mappedBy = "song", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<UserFavorite> favoriteSongs;

    public Song() {}
    public Song(Long id) { this.id = id; }
    public Song(String songName, String artistName) {
        this.songName = songName;
        this.artistName = artistName;
    }

    public Long getId() {
        return id;
    }
    public String getSongName() {
        return songName;
    }
    public String getArtistName() {
        return artistName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Song ID: " + getId());
        sb.append("Name of song: " + getSongName());
        sb.append("Name of artist: " + getArtistName());
        return sb.toString();
    }
}

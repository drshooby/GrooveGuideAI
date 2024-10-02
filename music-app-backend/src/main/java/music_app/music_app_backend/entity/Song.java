package music_app.music_app_backend.entity;

import jakarta.persistence.*;

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
    public Song(Long id) { setId(id); }
    public Song(String songName, String artistName) {
        setSongName(songName);
        setArtistName(artistName);
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSongName() {
        return songName;
    }

    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Set<UserFavorite> getFavoriteSongs() {
        return favoriteSongs;
    }

    public void setFavoriteSongs(Set<UserFavorite> favoriteSongs) {
        this.favoriteSongs = favoriteSongs;
    }
}

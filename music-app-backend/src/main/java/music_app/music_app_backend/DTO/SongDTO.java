package music_app.music_app_backend.DTO;

import music_app.music_app_backend.Entity.Song;

public class SongDTO {
    private Long id;
    private String songName;
    private String artistName;

    public SongDTO() {}

    public SongDTO(String songName, String artistName) {
        setSongName(songName);
        setArtistName(artistName);
    }

    public SongDTO(Long id, String songName, String artistName) {
        setId(id);
        setSongName(songName);
        setArtistName(artistName);
    }

    public Long getId() { return id; }
    public void setId(Long id) {
        this.id = id;
    }

    public String getSongName() { return songName; }
    public void setSongName(String songName) {
        this.songName = songName;
    }

    public String getArtistName() { return artistName; }
    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public static SongDTO fromEntity(Song song) {
        return new SongDTO(song.getId(), song.getSongName(), song.getArtistName());
    }

    @Override
    public String toString() {
        return songName + " by " + artistName;
    }

    public static SongDTO fromString(String input) {
        String[] li = input.split("by");
        return new SongDTO(li[0].trim(), li[1].trim());
    }
}

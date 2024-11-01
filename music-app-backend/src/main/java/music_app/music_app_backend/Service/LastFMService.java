package music_app.music_app_backend.Service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class LastFMService {

    @Value("${lastfm.api-key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();

    public String getAlbumImageURL(String artist, String track) {
        String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
        String encodedTrack = URLEncoder.encode(track, StandardCharsets.UTF_8);

        String url = String.format(
                "https://ws.audioscrobbler.com/2.0/?method=track.getinfo&api_key=%s&artist=%s&track=%s&format=json",
                apiKey, encodedArtist, encodedTrack);

        String response = restTemplate.getForObject(url, String.class);

        try {
            JSONObject json = new JSONObject(response);

            if (json.has("track")) {
                return json.getJSONObject("track")
                        .getJSONObject("album")
                        .getJSONArray("image")
                        .getJSONObject(2)
                        .getString("#text");
            }
        } catch (JSONException ignored) {}
        return "none";
    }
}

package music_app.music_app_backend.Service;

import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class DeezerService {

    private final RestTemplate restTemplate = new RestTemplate();

    public String[] getAlbumImageURL(String artist, String track) {
        String encodedArtist = URLEncoder.encode(artist, StandardCharsets.UTF_8);
        String encodedTrack = URLEncoder.encode(track, StandardCharsets.UTF_8);

        String url = String.format(
                "https://api.deezer.com/search/track?q=artist:\"%s\" track:\"%s\"",
                encodedArtist, encodedTrack);

        String response = restTemplate.getForObject(url, String.class);

        try {
            JSONObject json = new JSONObject(response);

            if (json.has("data") && json.getJSONArray("data").length() > 0) {
                JSONObject trackInfo = json.getJSONArray("data").getJSONObject(0);
                return new String[]{
                        trackInfo
                                .getJSONObject("album")
                                .getString("cover_big"),
                        trackInfo
                                .getString("link")
                };
            }
        } catch (JSONException ignored) {}
        return new String[]{};
    }
}

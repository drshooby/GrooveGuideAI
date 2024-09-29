package music_app.music_app_backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.entity.Song;
import org.springframework.stereotype.Service;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.service.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
public class LLMService {
    private final ChatLanguageModel chatLanguageModel;

    public LLMService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    interface AiRecommender {
        @UserMessage("Recommend three songs based on the user's following favorites: {{it}}." +
                "The response should be formatted as:\n" +
                "Song Name by Artist, Song Name by Artist, Song Name by Artist")
        String recommendBasedOnInput(String input);

        @UserMessage("Recommend three songs based on the user's following favorites: {{it}} and {{favoriteSongs}}." +
                "The response should be formatted as:\n" +
                "Song Name by Artist, Song Name by Artist, Song Name by Artist")
        String recommendBasedOnInputAndFavorites(String input, List<SongDTO> favoriteSongs);
    }

    public List<String> recommend(String input) {
        AiRecommender aiRecommender = AiServices.create(AiRecommender.class, chatLanguageModel);
        String recommendations = aiRecommender.recommendBasedOnInput(input);
        System.out.println(recommendations);
        String[] li = recommendations.split(",");

        List<String> songs = new ArrayList<>();
        for (String songByArtist : li) {
            songs.add(songByArtist.trim());
        }
        return songs;
    }


    public List<SongDTO> recommend(String input, List<SongDTO> favoriteSongs) {
        AiRecommender aiRecommender = AiServices.create(AiRecommender.class, chatLanguageModel);
        String recommendations = aiRecommender.recommendBasedOnInputAndFavorites(input, favoriteSongs);

        String[] li = recommendations.split(",");

        List<SongDTO> songs = new ArrayList<>();
        for (String songByArtist : li) {
            songs.add(SongDTO.fromString(songByArtist.trim()));
        }
        return songs;
    }
}


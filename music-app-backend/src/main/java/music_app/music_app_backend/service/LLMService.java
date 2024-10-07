package music_app.music_app_backend.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.model.output.structured.Description;
import music_app.music_app_backend.DTO.SongDTO;
import music_app.music_app_backend.entity.Song;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${langchain4j.open-ai.chat-model.api-key}")
    private String apiKey;

    public LLMService(ChatLanguageModel chatLanguageModel) {
        this.chatLanguageModel = chatLanguageModel;
    }

    class RecommendedSong {
        @Description("name of a recommended song")
        private String songName;
        @Description("name of an artist of the song")
        private String artistName;

        @Override
        public String toString() {
            return songName + " by " + artistName;
        }
    }

    interface AiRecommender {
        @UserMessage("Recommend three songs based on the user's following favorites: {{it}}." +
                "The response should be formatted as:\n" +
                "'Song Name by Artist, Song Name by Artist, Song Name by Artist'." +
                "There should be nothing else present in the response, no adding additional words, and do not acknowledge that you understand" +
                "the request, just please respond in the format specified, thanks.")
        String recommendBasedOnInput(String input);

        @UserMessage("Recommend three songs based on the user's following favorites: {{input}} and {{favoriteSongs}}." +
                "The response should be formatted as:\n" +
                "'Song Name by Artist, Song Name by Artist, Song Name by Artist'." +
                "There should be nothing else present in the response, no adding additional words, and do not acknowledge that you understand" +
                "the request, just please respond in the format specified, thanks.")
        String recommendBasedOnInputAndFavorites(@V("input") String input, @V("favoriteSongs") String favoriteSongs);

        @UserMessage("Recommend a song based on the user's following favorites: {{it}}.")
        RecommendedSong recommendAsStructuredOutput(String input);
    }

    public List<String> recommend(String input) {
        AiRecommender aiRecommender = AiServices.create(AiRecommender.class, chatLanguageModel);
        String recommendations = aiRecommender.recommendBasedOnInput(input);

        String pattern = "[\"']+";

        String[] li = recommendations.trim().replaceAll(pattern, "").split(",");

        List<String> songs = new ArrayList<>();
        for (String songByArtist : li) {
            songs.add(songByArtist.trim());
        }

        return songs;
    }


    public List<String> recommend(String input, String favoriteSongs) {
        AiRecommender aiRecommender = AiServices.create(AiRecommender.class, chatLanguageModel);
        String recommendations = aiRecommender.recommendBasedOnInputAndFavorites(input, favoriteSongs);

        String pattern = "[\"']+";

        String[] li = recommendations.trim().replaceAll(pattern, "").split(",");

        List<String> songs = new ArrayList<>();
        for (String songByArtist : li) {
            songs.add(songByArtist.trim());
        }
        return songs;
    }

    public List<String> recommendByPOJO(String input) {
        AiRecommender aiRecommender = AiServices.create(AiRecommender.class, chatLanguageModel);
        RecommendedSong song = aiRecommender.recommendAsStructuredOutput(input);

        List<String> songsStr = new ArrayList<>();
        System.out.println(song);

        return songsStr;
    }
}


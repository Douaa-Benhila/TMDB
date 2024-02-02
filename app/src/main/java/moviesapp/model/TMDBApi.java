package moviesapp.model;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class TMDBApi {

    private static final String API_KEY = "c2e5eea5f9078e7bd27be9838d32abf8";
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public static String sendGET(String endpoint, String queryParams) throws IOException {
        String urlString = BASE_URL + endpoint + "?api_key=" + API_KEY + queryParams;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

    public static String searchMovieByTitle(String title) throws IOException {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String endpoint = "/search/movie";
        String queryParams = "&query=" + encodedTitle;
        return sendGET(endpoint, queryParams);
    }

    public static Movie parseMovieFromSearchResult(String searchResult) {
        Gson gson = new Gson();
        MovieListResponse movieListResponse = gson.fromJson(searchResult, MovieListResponse.class);

        if (movieListResponse != null && movieListResponse.getResults() != null && !movieListResponse.getResults().isEmpty()) {
            // Supposons que le premier résultat est le bon
            return movieListResponse.getResults().get(0);
        } else {
            return null;
        }
    }


    public static List<String> searchMovieTitles(String title) throws IOException {
        String searchResult = sendGET("/search/movie", "&query=" + URLEncoder.encode(title, StandardCharsets.UTF_8));
        MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);

        if (movieListResponse != null && movieListResponse.getResults() != null) {
            return movieListResponse.getResults().stream()
                    .map(Movie::getTitle)
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }

}

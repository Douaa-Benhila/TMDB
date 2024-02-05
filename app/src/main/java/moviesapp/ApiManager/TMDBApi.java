package moviesapp.ApiManager;

import com.google.gson.Gson;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import moviesapp.model.Movie;

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

//La classe TMDBApi interagit avec l'API The Movie Database pour récupérer des informations liées aux films.
public class TMDBApi {

    private static final String API_KEY = "c2e5eea5f9078e7bd27be9838d32abf8";
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    //Envoie une requête GET à l'API TMDb avec l'endpoint et les paramètres de requête spécifiés.
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

    //Recherche un film par son titre en utilisant l'API TMDb.
    //l'endpoint est la partie de l'URL qui spécifie la ressource qu'on souhaite utiliser
    public static String searchMovieByTitle(String title) throws IOException {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String endpoint = "/search/movie";
        String queryParams = "&query=" + encodedTitle;
        return sendGET(endpoint, queryParams);
    }

    //Analyse un objet Movie à partir du résultat de recherche JSON obtenu de l'API TMDb
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

    //Recherche des titres de films en fonction d'un terme de recherche en utilisant l'API TMDb.
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

    // Existing methods...

    /**
     * Discover movies with specified search filters.
     *
     * @param queryParams The query parameters for filtering movies.
     * @return The JSON response string from TMDB API.
     * @throws IOException If an I/O error occurs.
     */
    public static String discoverMovies(String queryParams) throws IOException {
        return sendGET("/discover/movie", queryParams);
    }


    public static List<String> searchMoviesByFilters(String title, String genre, String year, String rating) throws IOException {
        StringBuilder queryParams = new StringBuilder();

        if (!title.isEmpty()) {
            queryParams.append("&query=").append(URLEncoder.encode(title, StandardCharsets.UTF_8));
        }

        if (!genre.isEmpty()) {
            queryParams.append("&with_genres=").append(genre);
        }

        if (!year.isEmpty()) {
            queryParams.append("&primary_release_year=").append(year);
        }

        if (!rating.isEmpty()) {
            queryParams.append("&vote_average.gte=").append(rating);
        }

        String searchResult = sendGET("/search/movie", queryParams.toString());
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

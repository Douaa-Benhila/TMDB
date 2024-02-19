package moviesapp.ApiManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import moviesapp.model.Movie;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


//La classe TMDBApi interagit avec l'API The Movie Database pour récupérer des informations liées aux films.
public class TMDBApi {

    private static HttpClient httpClient = HttpClient.newHttpClient();


    public static final String API_KEY = "c2e5eea5f9078e7bd27be9838d32abf8";
    public static final String BASE_URL = "https://api.themoviedb.org/3";

    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"; // Example base URL for width 500px images


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

    // ... other methods ...

    /**
     * Fetches the most popular movies from TMDb.
     *
     * @return A list of Movie objects representing the most popular movies.
     * @throws IOException If an I/O error occurs.
     */
    public static List<Movie> getPopularMovies() throws IOException {
        String response = sendGET("/movie/popular", "");
        MovieListResponse movieListResponse = JsonParser.parseMovieList(response);

        if (movieListResponse != null && movieListResponse.getResults() != null) {
            return movieListResponse.getResults();
        } else {
            return Collections.emptyList();
        }
    }


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



    public static List<Movie> searchMoviesByTitleAndFilter(String title, String genre, String startYear, String endYear, String rating) throws IOException {
        // Search by title
        String searchResult = sendGET("/search/movie", "&query=" + URLEncoder.encode(title, StandardCharsets.UTF_8));
        MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);

        if (movieListResponse == null || movieListResponse.getResults() == null) {
            return Collections.emptyList();
        }

        // Filter the results manually
        Stream<Movie> filteredStream = movieListResponse.getResults().stream();

        if (!genre.isEmpty()) {
            filteredStream = filteredStream.filter(movie -> movie.getGenre_ids().contains(Integer.parseInt(genre)));
        }
        if (!startYear.isEmpty()) {
            filteredStream = filteredStream.filter(movie -> movie.getRelease_date().compareTo(startYear + "-01-01") >= 0);
        }
        if (!endYear.isEmpty()) {
            filteredStream = filteredStream.filter(movie -> movie.getRelease_date().compareTo(endYear + "-12-31") <= 0);
        }
        if (!rating.isEmpty()) {
            filteredStream = filteredStream.filter(movie -> movie.getVote_average() >= Double.parseDouble(rating));
        }

        return filteredStream.collect(Collectors.toList());
    }
    public static List<Movie> discoverMoviesWithFilters(String genre, String startYear, String endYear, String rating) throws IOException {
        StringBuilder queryParams = new StringBuilder();

        if (!genre.isEmpty()) {
            queryParams.append("&with_genres=").append(genre);
        }
        if (!startYear.isEmpty()) {
            queryParams.append("&primary_release_date.gte=").append(startYear + "-01-01");
        }
        if (!endYear.isEmpty()) {
            queryParams.append("&primary_release_date.lte=").append(endYear + "-12-31");
        }
        if (!rating.isEmpty()) {
            queryParams.append("&vote_average.gte=").append(rating);
        }

        String searchResult = sendGET("/discover/movie", queryParams.toString());
        MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);

        if (movieListResponse != null && movieListResponse.getResults() != null) {
            return movieListResponse.getResults();
        } else {
            return Collections.emptyList();
        }
    }
    public static List<Movie> searchMovies(String title, String genre, String startYear, String endYear, String rating) throws IOException {
        if (title != null && !title.trim().isEmpty()) {
            // If title is provided, use the search by title method and then filter results
            return searchMoviesByTitleAndFilter(title.trim(), genre, startYear, endYear, rating);
        } else {
            // If no title is provided, use the discover method with filters
            return discoverMoviesWithFilters(genre, startYear, endYear, rating);
        }
    }
}
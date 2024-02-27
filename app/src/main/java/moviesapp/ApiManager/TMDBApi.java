package moviesapp.ApiManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import moviesapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * Fournit des méthodes statiques pour interagir avec l'API de The Movie Database (TMDb).
 * Permet de rechercher des films, découvrir des films selon des critères spécifiques, obtenir des films populaires,
 * et récupérer des informations détaillées sur les films et leurs genres.
 */public class TMDBApi {
    // Constantes pour l'API
    private final static HttpClient httpClient = HttpClient.newHttpClient();
    public static final String API_KEY = "c2e5eea5f9078e7bd27be9838d32abf8";
    public static final String BASE_URL = "https://api.themoviedb.org/3";
    public static final String IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500";

    /**
     * Envoie une requête GET à l'API TMDb.
     *
     * @param endpoint    Le point de terminaison spécifique de l'API à appeler.
     * @param queryParams Les paramètres de la requête à ajouter à l'URL.
     * @return La réponse de l'API sous forme de chaîne de caractères JSON.
     * @throws IOException Si une erreur d'entrée/sortie se produit pendant l'envoi de la requête.
     */    public static String sendGET(String endpoint, String queryParams) throws IOException {
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

    /**
     * Recherche des films par titre.
     *
     * @param title Le titre du film à rechercher.
     * @return La réponse de l'API sous forme de chaîne de caractères JSON.
     * @throws IOException Si une erreur d'entrée/sortie se produit pendant la recherche.
     */
    public static String searchMovieByTitle(String title) throws IOException {
        String encodedTitle = URLEncoder.encode(title, StandardCharsets.UTF_8);
        String endpoint = "/search/movie";
        String queryParams = "&query=" + encodedTitle;
        return sendGET(endpoint, queryParams);
    }

    /**
     * Analyse les détails d'un film à partir d'une réponse JSON.
     *
     * @param searchResult La chaîne JSON contenant les résultats de la recherche.
     * @return Un objet Movie représentant le premier film trouvé dans les résultats de la recherche.
     */
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

    /**
     * Obtient les films populaires.
     *
     * @return Une liste d'objets Movie représentant les films populaires.
     * @throws IOException Si une erreur d'entrée/sortie se produit lors de la récupération des films populaires.
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
     * Découvre des films selon des filtres spécifiés.
     *
     * @param queryParams Les paramètres de requête pour filtrer les films.
     * @return La réponse de l'API sous forme de chaîne de caractères JSON.
     * @throws IOException Si une erreur d'entrée/sortie se produit pendant la découverte des films.
     */
    public static String discoverMovies(String queryParams) throws IOException {
        return sendGET("/discover/movie", queryParams);
    }

    /**
     * Recherche des films en fonction du titre, du genre, de l'année de début, de l'année de fin et du classement.
     * Si un titre est fourni, la recherche s'effectue par titre puis filtre les résultats. Sinon, découvre les films avec les filtres fournis.
     *
     * @param title Le titre du film à rechercher.
     * @param genre Le genre du film.
     * @param startYear L'année de début pour filtrer les films.
     * @param endYear L'année de fin pour filtrer les films.
     * @param rating Le classement minimal des films.
     * @return Une liste des films correspondant aux critères de recherche.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     */
    public static List<Movie> searchMovies(String title, String genre, String startYear, String endYear, String rating) throws IOException {
        if (title != null && !title.trim().isEmpty()) {
            // If title is provided, use the search by title method and then filter results
            return searchMoviesByTitleAndFilter(title.trim(), genre, startYear, endYear, rating);
        } else {
            // If no title is provided, use the discover method with filters
            return discoverMoviesWithFilters(genre, startYear, endYear, rating);
        }
    }

    /**
     * Découvre des films en appliquant des filtres de genre, d'année de début, d'année de fin et de classement.
     *
     * @param genre Le genre du film.
     * @param startYear L'année de début pour filtrer les films.
     * @param endYear L'année de fin pour filtrer les films.
     * @param rating Le classement minimal des films.
     * @return Une liste des films correspondant aux critères de filtre.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     */
    public static List<Movie> discoverMoviesWithFilters(String genre, String startYear, String endYear, String rating) throws IOException {
        StringBuilder queryParams = new StringBuilder();

        if (!genre.isEmpty()) {
            queryParams.append("&with_genres=").append(genre);
        }
        if (!startYear.isEmpty()) {
            queryParams.append("&primary_release_date.gte=").append(startYear).append("-01-01");
        }
        if (!endYear.isEmpty()) {
            queryParams.append("&primary_release_date.lte=").append(endYear).append("-12-31");
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

    /**
     * Découvre des films en appliquant des filtres de genre, d'année de début, d'année de fin et de classement.
     *
     * @param genre Le genre du film.
     * @param startYear L'année de début pour filtrer les films.
     * @param endYear L'année de fin pour filtrer les films.
     * @param rating Le classement minimal des films.
     * @return Une liste des films correspondant aux critères de filtre.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     */
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

    /**
     * Obtient des films similaires à un film donné.
     *
     * @param movieId L'identifiant du film pour lequel trouver des films similaires.
     * @return Une liste des films similaires.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     */
    public static List<Movie> getRelatedMovies(int movieId) throws IOException {
        // Construct the endpoint for fetching related movies
        String endpoint = "/movie/" + movieId + "/similar";
        // Use the sendGET method to make the API call
        String response = sendGET(endpoint, "");

        // Parse the response
        Gson gson = new Gson();
        // Assuming MovieListResponse is structured to parse the list of movies from the response
        MovieListResponse movieListResponse = gson.fromJson(response, MovieListResponse.class);

        if (movieListResponse != null && movieListResponse.getResults() != null) {
            return movieListResponse.getResults();
        } else {
            return Collections.emptyList();
        }
    }

    /**
     * Récupère une liste des genres de films disponibles.
     *
     * @return Une carte des genres de films, où la clé est l'identifiant du genre et la valeur est le nom du genre.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     * @throws InterruptedException Si l'opération est interrompue.
     */
    public static Map<Integer, String> fetchMovieGenres() throws IOException, InterruptedException {
        Map<Integer, String> genresMap = new HashMap<>();
        String url = BASE_URL + "/genre/movie/list?api_key=" + API_KEY + "&language=en-US";

        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        JSONObject responseObject = new JSONObject(response.body());
        JSONArray genresArray = responseObject.getJSONArray("genres");
        for (int i = 0; i < genresArray.length(); i++) {
            JSONObject genre = genresArray.getJSONObject(i);
            int id = genre.getInt("id");
            String name = genre.getString("name");
            genresMap.put(id, name);
        }

        return genresMap;
    }

    /**
     * Obtient une liste de films dirigés par un réalisateur spécifique.
     *
     * @param directorId L'identifiant du réalisateur.
     * @return Une liste des titres de films dirigés par le réalisateur.
     * @throws IOException Si une erreur se produit lors de la communication avec l'API.
     * @throws InterruptedException Si l'opération est interrompue.
     */


}
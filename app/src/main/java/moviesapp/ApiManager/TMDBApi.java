package moviesapp.ApiManager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import moviesapp.model.Author;
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


    public static List<Movie> searchMoviesByFilters(String title, String genre, String year, String rating) throws IOException {
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

    public static String getDirectorName(int movieId) throws IOException, InterruptedException {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Utilisation de Gson pour parser la chaîne JSON
        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        JsonArray crew = jsonResponse.getAsJsonArray("crew");

        for (JsonElement element : crew) {
            JsonObject crewMember = element.getAsJsonObject();
            if ("Director".equals(crewMember.get("job").getAsString())) {
                return crewMember.get("name").getAsString(); // Retourne le nom du premier réalisateur trouvé
            }
        }

        return "Inconnu"; // Retourne "Inconnu" si aucun réalisateur n'est trouvé
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


    public static String[] getDirectorDetails(int movieId) throws IOException, InterruptedException {
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);
        JsonArray crew = jsonResponse.getAsJsonArray("crew");

        for (JsonElement element : crew) {
            JsonObject crewMember = element.getAsJsonObject();
            if ("Director".equals(crewMember.get("job").getAsString())) {
                String name = crewMember.get("name").getAsString();
                String id = crewMember.get("id").getAsString(); // Extrait l'ID
                return new String[]{name, id}; // Retourne le nom et l'ID
            }
        }

        return null; // Ou une valeur par défaut
    }

    public static Author getDirectorDetails(String directorId) throws IOException, InterruptedException {
        String url = BASE_URL + "/person/" + directorId + "?api_key=" + API_KEY;
        HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).GET().build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        Gson gson = new Gson();
        JsonObject jsonResponse = gson.fromJson(response.body(), JsonObject.class);

        // Utilisez JsonElement pour éviter JsonNull
        JsonElement nameElement = jsonResponse.get("name");
        String name = (nameElement != null && !nameElement.isJsonNull()) ? nameElement.getAsString() : "Name not available";

        JsonElement biographyElement = jsonResponse.get("biography");
        String biography = (biographyElement != null && !biographyElement.isJsonNull()) ? biographyElement.getAsString() : "Biography not available";

        JsonElement imagePathElement = jsonResponse.get("profile_path");
        String imagePath = (imagePathElement != null && !imagePathElement.isJsonNull()) ? "https://image.tmdb.org/t/p/w500" + imagePathElement.getAsString() : "";

        return new Author(name, biography, imagePath);
    }




}

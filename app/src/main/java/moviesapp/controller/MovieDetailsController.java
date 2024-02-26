package moviesapp.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Author;
import moviesapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static moviesapp.ApiManager.TMDBApi.API_KEY;

/**
 * Contrôleur pour l'affichage des détails d'un film dans une application JavaFX.
 */
public class MovieDetailsController {
    // Éléments de l'interface utilisateur déclarés dans le fichier FXML
    @FXML
    private ImageView backdropImageView;
    @FXML
    private ImageView posterImageView;
    @FXML
    private Label titleLabel;
    @FXML
    private Label releaseDateLabel;
    @FXML
    private Label originalTitleLabel;
    @FXML
    private Label originalLanguageLabel;
    @FXML
    private Label genreLabel;
    @FXML
    private Label overviewText;
    @FXML
    private Label popularityLabel;
    @FXML
    private Label voteAverageLabel;
    @FXML
    private Label voteCountLabel;
    @FXML
    private Label adultLabel;
    @FXML
    private Label videoLabel;
    @FXML
    private Hyperlink directorHyperlink;
    @FXML
    private FlowPane actorsSection;
    @FXML
    private FlowPane relatedMoviesSection;

    // Références pour les actions de l'utilisateur
    private Consumer<Movie> onAddToFavorites;
    private Consumer<Movie> onRemoveFromFavorites;
    private Consumer<Movie> onDisplayMovieDetails;
    private String directorId;

    /**
     * Initialise le contrôleur avec les détails d'un film spécifique.
     *
     * @param movie Le film dont les détails doivent être affichés.
     */
    public void setMovieDetails(Movie movie) {
        String imageOriginalPath = "https://image.tmdb.org/t/p/original"; // Base path for images from TMDB (adjust as necessary)
        String imagePath = "https://image.tmdb.org/t/p/w500"; // Base path for images from TMDB (adjust as necessary)
        backdropImageView.setImage(new Image(imageOriginalPath + movie.getBackdrop_path(), true));
        posterImageView.setImage(new Image(imagePath + movie.getPoster_path(), true));
        titleLabel.setText(movie.getTitle());
        releaseDateLabel.setText("- Release Date: " + movie.getRelease_date());
        originalTitleLabel.setText("- Original Title: " + movie.getOriginal_title());
        originalLanguageLabel.setText("- Language: " + movie.getOriginal_language().toUpperCase());
        updateGenreLabel(movie.getGenre_ids());
        overviewText.setText("- Overview: " + movie.getOverview());
        popularityLabel.setText("- Popularity: " + String.format("%.1f", movie.getPopularity()));
        voteAverageLabel.setText("- Rating: " + String.format("%.1f", movie.getVote_average()) + " / 10");
        voteCountLabel.setText("- Votes: " + movie.getVote_count());
        adultLabel.setText("- Adult: " + (movie.isAdult() ? "Yes" : "No"));
        videoLabel.setText("- Video: " + (movie.isVideo() ? "Yes" : "No"));

        fetchActors(movie.getId());
        displayRelatedMovies(movie);
        updateDirectorDetails(movie.getId());
    }

    public void updateDirectorDetails(int movieId) {
        new Thread(() -> {
            try {
                String[] directorDetails = TMDBApi.getDirectorDetails(movieId);
                if (directorDetails != null) {
                    String directorName = directorDetails[0];
                    String directorId = directorDetails[1];
                    Platform.runLater(() -> {
                        directorHyperlink.setText("Director: " + directorName);
                        // Stockage de directorId pour l'utiliser lors de l'affichage des films du réalisateur
                        this.directorId = directorId;
                    });
                }
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> directorHyperlink.setText("Director: Error loading"));
            }
        }).start();
    }

    /**
     * Met à jour le label des genres du film à partir d'une liste d'identifiants de genres.
     * Cela pourrait nécessiter un appel API pour convertir les identifiants en noms de genre.
     *
     * @param genreIds La liste des identifiants de genres du film.
     */
    private void updateGenreLabel(List<Integer> genreIds) {
        Task<String> fetchGenresTask = new Task<>() {
            @Override
            protected String call() throws Exception {
                Map<Integer, String> genresMap = TMDBApi.fetchMovieGenres();
                return genreIds.stream()
                        .map(id -> genresMap.getOrDefault(id, "Unknown"))
                        .collect(Collectors.joining(", "));
            }
        };

        fetchGenresTask.setOnSucceeded(e -> genreLabel.setText("- Genres: " + fetchGenresTask.getValue()));
        fetchGenresTask.setOnFailed(e -> genreLabel.setText("- Genres: Unknown"));

        new Thread(fetchGenresTask).start();
    }

    /**
     * Récupère les acteurs d'un film spécifique à partir de l'API TMDB et met à jour l'interface utilisateur.
     * Cette méthode envoie une requête HTTP asynchrone pour obtenir les crédits du film, y compris les acteurs,
     * puis analyse la réponse pour afficher les images et les noms des acteurs dans la section des acteurs.
     *
     * @param movieId L'identifiant du film pour lequel récupérer les acteurs.
     */
    private void fetchActors(int movieId) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/credits?api_key=" + API_KEY;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(this::parseAndDisplayActors)
                .join();
    }

    /**
     * Analyse la réponse JSON contenant les acteurs du film et met à jour l'interface utilisateur.
     * Cette méthode est appelée de manière asynchrone après la récupération des données de l'API TMDB.
     *
     * @param responseBody Le corps de la réponse de l'API sous forme de chaîne JSON.
     */
    private void parseAndDisplayActors(String responseBody) {
        JSONObject jsonObject = new JSONObject(responseBody);
        JSONArray castArray = jsonObject.getJSONArray("cast");
        String imageBaseURL = "https://image.tmdb.org/t/p/w185"; // Base URL for actor images

        Platform.runLater(() -> {
            actorsSection.getChildren().clear();
            for (int i = 0; i < castArray.length(); i++) {
                JSONObject actor = castArray.getJSONObject(i);
                String actorName = actor.getString("name");
                String profilePath = actor.optString("profile_path", ""); // Use optString to handle null profile_path
                Image actorImage = new Image(imageBaseURL + profilePath, true); // true for background loading

                ImageView imageView = new ImageView(actorImage);
                imageView.setFitWidth(100); // Set preferred width
                imageView.setFitHeight(150); // Set preferred height
                imageView.setPreserveRatio(true);

                Label actorLabel = new Label(actorName);
                actorLabel.getStyleClass().add("actor-name");

                VBox actorContainer = new VBox(7, imageView, actorLabel); // 5 is the spacing between elements
                actorContainer.setAlignment(Pos.CENTER);

                actorsSection.getChildren().add(actorContainer);
            }
        });
    }

    /**
     * Affiche les films liés à un film spécifique.
     * Cette méthode lance une tâche asynchrone qui récupère les films liés à partir de l'API TMDB,
     * puis met à jour l'interface utilisateur avec les films liés en utilisant des vignettes de film.
     *
     * @param movie Le film pour lequel afficher les films liés.
     */
    public void displayRelatedMovies(Movie movie) {
        Task<List<Movie>> task = new Task<>() {
            @Override
            protected List<Movie> call() throws Exception {
                return TMDBApi.getRelatedMovies(movie.getId());
            }
        };

        task.setOnSucceeded(e -> {
            relatedMoviesSection.getChildren().clear();
            for (Movie relatedMovie : task.getValue()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));
                    VBox movieTile = fxmlLoader.load();
                    MovieTileController controller = fxmlLoader.getController();
                    // Ensure the setMovie method correctly initializes the movie tile
                    // Adjust the callback methods as necessary for your application's functionality
                    controller.setMovie(relatedMovie, onAddToFavorites, onRemoveFromFavorites, onDisplayMovieDetails);
                    relatedMoviesSection.getChildren().add(movieTile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        task.setOnFailed(e -> System.out.println("Failed to fetch related movies: " + task.getException()));

        new Thread(task).start();
    }

    /**
     * Configure les actions pour les interactions de l'utilisateur avec les favoris.
     *
     * @param add    Action à exécuter lorsque l'utilisateur souhaite ajouter un film aux favoris.
     * @param remove Action à exécuter lorsque l'utilisateur souhaite retirer un film des favoris.
     */
    public void setFavoritesActions(Consumer<Movie> add, Consumer<Movie> remove, Consumer<Movie> displayDetails) {
        this.onAddToFavorites = add;
        this.onRemoveFromFavorites = remove;
        this.onDisplayMovieDetails = displayDetails;
    }

    /**
     * Affiche une nouvelle fenêtre contenant la liste des films d'un réalisateur spécifique.
     * Cette méthode charge une vue FXML pour les films du réalisateur, initialise le contrôleur
     * associé avec l'identifiant du réalisateur, et configure une nouvelle scène et un nouveau stage
     * pour afficher cette vue. La fenêtre est configurée pour être non redimensionnable et a des dimensions minimales.
     *
     * @param directorId L'identifiant du réalisateur pour lequel charger et afficher les films.
     */
    private void showDirectorMovies(String directorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DirectorMoviesView.fxml"));

            Parent root = loader.load();

            VBox layout = new VBox(10);
            layout.setPadding(new Insets(15, 20, 10, 10));
            layout.getChildren().add(root);

            DirectorMoviesController controller = loader.getController();
            controller.loadMovies(directorId);

            Stage stage = new Stage();
            stage.setTitle("Films du realisateur");

            Scene scene = new Scene(layout);
            stage.setScene(scene);

            stage.setMinWidth(600); // Minimum width
            stage.setMinHeight(400); // Minimum height
            stage.setResizable(false); // Making the stage non-resizable

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gère l'action de clic sur le lien du réalisateur, permettant d'afficher d'autres films de ce réalisateur.
     */
    public void handleDirectorHyperlinkAction(javafx.event.ActionEvent actionEvent) {
        showDirectorMovies(directorId);
    }
}


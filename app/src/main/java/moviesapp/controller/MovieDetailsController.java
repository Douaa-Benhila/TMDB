package moviesapp.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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
import javafx.scene.text.Font;
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

public class MovieDetailsController {

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
    private String directorId;

    @FXML
    private Label directorNameLabel;
    @FXML
    private Label directorBiographyLabel;
    @FXML
    private ImageView directorImageView;
    @FXML
    private VBox moviesVBox;

    @FXML
    private StackPane mainContentArea;




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


    @FXML
    private FlowPane actorsSection;

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

    @FXML
    private FlowPane relatedMoviesSection;
    private Consumer<Movie> onAddToFavorites;
    private Consumer<Movie> onRemoveFromFavorites;
    private Consumer<Movie> onDisplayMovieDetails;


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

    // In MovieDetailsController
    public void setFavoritesActions(Consumer<Movie> add, Consumer<Movie> remove, Consumer<Movie> displayDetails) {
        this.onAddToFavorites = add;
        this.onRemoveFromFavorites = remove;
        this.onDisplayMovieDetails = displayDetails;
    }





    private void showDirectorMovies(String directorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DirectorMoviesView.fxml"));

            Parent root = loader.load();

            DirectorMoviesController controller = loader.getController();
            controller.loadMovies(directorId);
            Stage stage = new Stage();
            stage.setTitle("Films du réalisateur");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleDirectorHyperlinkAction(javafx.event.ActionEvent actionEvent) {
        showDirectorMovies(directorId);

    }
}













    /*private void showDirectorDetails(String directorId) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DirectorMoviesView.fxml"));
            Parent root = loader.load();

            DirectorMoviesController controller = loader.getController();

            // Supposons que vous ayez une fonction pour obtenir les détails du réalisateur par son ID
            Author director = (Author) TMDBApi.getMoviesByDirectorId(directorId);
            controller.setDirectorDetails(director);

            // Récupérer les films du réalisateur et les afficher
            List<Movie> directorMovies = TMDBApi.getMoviesByDirectorId(directorId);
            controller.displayDirectorMovies((Movie) directorMovies);

            Stage stage = new Stage();
            stage.setTitle(director.getName());
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void handleDirectorHyperlinkAction(javafx.event.ActionEvent actionEvent) {
        showDirectorDetails(directorId);
    }*/


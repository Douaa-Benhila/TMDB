package moviesapp.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.JsonManager.JsonParser;
import moviesapp.model.Movie;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static moviesapp.ApiManager.TMDBApi.API_KEY;

public class MovieDetailsController {

    @FXML private ImageView backdropImageView;
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label originalTitleLabel;
    @FXML private Label originalLanguageLabel;
    @FXML private Label genreLabel;
    @FXML private Label overviewText;
    @FXML private Label popularityLabel;
    @FXML private Label voteAverageLabel;
    @FXML private Label voteCountLabel;
    @FXML private Label adultLabel;
    @FXML private Label videoLabel;
    @FXML private Label directorLabel;


    public void setMovieDetails(Movie movie) {
        String imageOriginalPath = "https://image.tmdb.org/t/p/original"; // Base path for images from TMDB (adjust as necessary)
        String imagePath = "https://image.tmdb.org/t/p/w500"; // Base path for images from TMDB (adjust as necessary)
        backdropImageView.setImage(new Image(imageOriginalPath + movie.getBackdrop_path(), true));
        posterImageView.setImage(new Image(imagePath + movie.getPoster_path(), true));
        titleLabel.setText(movie.getTitle());
        releaseDateLabel.setText("- Release Date: " + movie.getRelease_date());
        originalTitleLabel.setText("- Original Title: " + movie.getOriginal_title());
        originalLanguageLabel.setText("- Language: " + movie.getOriginal_language().toUpperCase());
        genreLabel.setText("- Genres: " + movie.getGenre_ids().toString());
        overviewText.setText("- Overview: " + movie.getOverview());
        popularityLabel.setText("- Popularity: " + movie.getPopularity());
        voteAverageLabel.setText("- Rating: " + movie.getVote_average() + " / 10");
        voteCountLabel.setText("- Votes: " + movie.getVote_count());
        adultLabel.setText("- Adult: " + (movie.isAdult() ? "Yes" : "No"));
        videoLabel.setText("- Video: " + (movie.isVideo() ? "Yes" : "No"));

        updateDirectorName(movie.getId());
        fetchActors(movie.getId());
    }
    public void updateDirectorName(int movieId) {
        new Thread(() -> {
            try {
                String directorName = TMDBApi.getDirectorName(movieId);
                Platform.runLater(() -> directorLabel.setText("- Director: " + directorName));
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> directorLabel.setText("Director: Error loading"));
            }
        }).start();
    }

    @FXML private FlowPane actorsSection;

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

}
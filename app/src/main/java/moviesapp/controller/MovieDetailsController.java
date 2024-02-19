package moviesapp.controller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;

import java.io.IOException;

public class MovieDetailsController {

    @FXML private ImageView backdropImageView;
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label originalTitleLabel;
    @FXML private Label originalLanguageLabel;
    @FXML private Label genreLabel;
    @FXML private Text overviewText;
    @FXML private Label popularityLabel;
    @FXML private Label voteAverageLabel;
    @FXML private Label voteCountLabel;
    @FXML private Label adultLabel;
    @FXML private Label videoLabel;
    @FXML private Label directorLabel;
    @FXML private Hyperlink directorHyperlink;
    private String directorId;

    public void setMovieDetails(Movie movie) {
        String imagePath = "https://image.tmdb.org/t/p/w500"; // Base path for images from TMDB (adjust as necessary)
        backdropImageView.setImage(new Image(imagePath + movie.getBackdrop_path(), true));
        posterImageView.setImage(new Image(imagePath + movie.getPoster_path(), true));
        titleLabel.setText(movie.getTitle());
        releaseDateLabel.setText("Release Date: " + movie.getRelease_date());
        originalTitleLabel.setText("Original Title: " + movie.getOriginal_title());
        originalLanguageLabel.setText("Language: " + movie.getOriginal_language().toUpperCase());
        genreLabel.setText("Genres: " + movie.getGenre_ids().toString());
        overviewText.setText(movie.getOverview());
        popularityLabel.setText("Popularity: " + movie.getPopularity());
        voteAverageLabel.setText("Rating: " + movie.getVote_average() + " / 10");
        voteCountLabel.setText("Votes: " + movie.getVote_count());
        adultLabel.setText("Adult: " + (movie.isAdult() ? "Yes" : "No"));
        videoLabel.setText("Video: " + (movie.isVideo() ? "Yes" : "No"));

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


    @FXML
    private void handleDirectorHyperlinkAction(ActionEvent event) {
        showDirectorMovies(directorId);
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





}






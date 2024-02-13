package moviesapp.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    @FXML
    private TextField titleField, genreField, startYearField, endYearField, ratingField;
    @FXML
    private FlowPane resultsSection;
    @FXML
    private Label sectionTitle; // Reference to the title label
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));

    private List<Movie> favoriteMovies = new ArrayList<>(); // Liste pour stocker les films favoris


    @FXML
    private void onSearch() {

        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String startYear = startYearField.getText().trim();
        String endYear = endYearField.getText().trim();

        try {
            sectionTitle.setText("-> Search Results");
            List<Movie> searchResults = TMDBApi.searchMoviesByFilters(title, genre, startYear, endYear);
            resultsSection.getChildren().clear();

            if (searchResults.isEmpty()) {
                resultsSection.getChildren().add(new Label("No movies found with the given criteria."));
            } else {
                for (Movie movie : searchResults) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/moviesapp.fxml"));
                        VBox movieTile = fxmlLoader.load();
                        resultsSection.getChildren().add(movieTile);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showMovieDetails(Movie movie) {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(movie.getTitle()).append("\n");
        details.append("Release Date: ").append(movie.getRelease_date()).append("\n");
        details.append("Overview: ").append(movie.getOverview()).append("\n");
        details.append("Adult: ").append(movie.isAdult()).append("\n");
        details.append("Genre IDs: ").append(movie.getGenre_ids()).append("\n");
        details.append("Original Language: ").append(movie.getOriginal_language()).append("\n");
        details.append("Original Title: ").append(movie.getOriginal_title()).append("\n");
        details.append("Popularity: ").append(movie.getPopularity()).append("\n");
        details.append("Video: ").append(movie.isVideo()).append("\n");
        details.append("Vote Average: ").append(movie.getVote_average()).append("\n");
        details.append("Vote Count: ").append(movie.getVote_count()).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Movie Details");
        alert.setHeaderText(null);
        alert.setContentText(details.toString());
        alert.showAndWait();
    }
    public void addToFavorites(Movie movie) {
        if (!favoriteMovies.contains(movie)) {
            movie.setFavorite(true);
            favoriteMovies.add(movie);
        }
    }

    private void removeFromFavorites(Movie movie) {
        if (favoriteMovies.contains(movie)) {
            movie.setFavorite(false);
            favoriteMovies.remove(movie);
        }
    }
}

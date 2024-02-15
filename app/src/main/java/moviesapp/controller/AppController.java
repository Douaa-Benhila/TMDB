package moviesapp.controller;

import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;

import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AppController {

    @FXML
    private TextField titleField, genreField, startYearField, endYearField, ratingField;
    @FXML
    private FlowPane resultsSection;
    @FXML
    private Label sectionTitle;

    public static List<Movie> favoriteMovies = new ArrayList<>(); // Liste pour stocker les films favoris

    private List<MovieTileController> movieTileControllers = new ArrayList<>();

    @FXML
    public void initialize() {displayMostViralMovies();}

    @FXML
    private void onSearch() {

        String title = titleField.getText().trim();
        String genre = genreField.getText().trim();
        String startYear = startYearField.getText().trim();
        String endYear = endYearField.getText().trim();
        String rating = ratingField.getText().trim();

        try {
            sectionTitle.setText("-> Search Results");
            List<Movie> searchResults = TMDBApi.searchMoviesByFilters(title, genre, startYear, endYear);
            resultsSection.getChildren().clear();

            if (searchResults.isEmpty()) {
                resultsSection.getChildren().add(new Label("No movies found with the given criteria."));
            } else {
                for (Movie movie : searchResults) {
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));
                        VBox movieTile = fxmlLoader.load();
                        MovieTileController controller = fxmlLoader.getController();
                        controller.setMovie(movie, this::addToFavorites, this::removeFromFavorites, this::displayMovieDetails);
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


    @FXML
    private void onShowFavorites() {
        // Clear the current results to make space for the favorites list.
        resultsSection.getChildren().clear();
        sectionTitle.setText("-> Favourite section :");

        if (favoriteMovies.isEmpty()) {
            resultsSection.getChildren().add(new Label("No favorites added."));
        } else {
            for (Movie movie : favoriteMovies) {
                try {
                    // Load the MovieTile for each favorite movie
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));
                    VBox movieTile = fxmlLoader.load();

                    // Get the controller and set the movie and the favorite state
                    MovieTileController controller = fxmlLoader.getController();
                    controller.setMovie(movie, this::addToFavorites, this::removeFromFavorites, this::displayMovieDetails);
                    controller.updateButtonAppearance(); // Call this method to ensure the correct appearance of the button

                    // Add the loaded tile to the results section
                    resultsSection.getChildren().add(movieTile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
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

    public void displayMostViralMovies() {
        Task<List<Movie>> task = new Task<>() {
            @Override
            protected List<Movie> call() throws Exception {
                return TMDBApi.getPopularMovies();
            }
        };



        task.setOnSucceeded(e -> {
            resultsSection.getChildren().clear();


            for (Movie movie : task.getValue()) {
                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));
                    VBox movieTile = fxmlLoader.load();
                    MovieTileController controller = fxmlLoader.getController();
                    controller.setMovie(movie, this::addToFavorites, this::removeFromFavorites, this::displayMovieDetails);
                    resultsSection.getChildren().add(movieTile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        task.setOnFailed(e -> {
            System.out.println("Failed to fetch popular movies: " + task.getException());
        });

        new Thread(task).start();
    }

    public void displayMovieDetails(Movie movie) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MovieDetails.fxml"));
            Node detailsView = loader.load();
            MovieDetailsController controller = loader.getController();
            controller.setMovieDetails(movie);

            // Ensure resultsSection is cleared or directly set the content of the ScrollPane
            resultsSection.getChildren().clear(); // Clear the tiles
            resultsSection.getChildren().add(detailsView); // Add the details view

            sectionTitle.setText("Movie Details"); // Update the section title accordingly

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

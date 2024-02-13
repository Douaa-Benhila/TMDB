package moviesapp.controller;

import javafx.application.Application;
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

public class AppController extends Application {

    private List<Movie> favoriteMovies = new ArrayList<>(); // Liste pour stocker les films favoris

    @Override
    public void start(Stage primaryStage) {
        VBox filtersSection = new VBox(10);
        filtersSection.setPadding(new Insets(10));
        TextField titleField = new TextField();
        titleField.setPromptText("Enter movie title");
        TextField genreField = new TextField();
        genreField.setPromptText("Enter genre");
        TextField startYearField = new TextField();
        startYearField.setPromptText("Start year");
        TextField endYearField = new TextField();
        endYearField.setPromptText("End year");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating");
        Button searchButton = new Button("Search");
        Button favoritesButton = new Button("Favorites"); // Bouton pour afficher les favoris
        filtersSection.getChildren().addAll(new Label("FILTERS:"), titleField, genreField, ratingField, startYearField, endYearField, searchButton, favoritesButton); // Ajout du bouton "Favorites"
        VBox resultsSection = new VBox(10);
        resultsSection.setPadding(new Insets(10));

        searchButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String startYear = startYearField.getText().trim();
            String endYear = endYearField.getText().trim();

            try {
                List<Movie> searchResults = TMDBApi.searchMoviesByFilters(title, genre, startYear, endYear);

                if (searchResults.isEmpty()) {
                    resultsSection.getChildren().clear();
                    resultsSection.getChildren().add(new Label("No movies found with the given criteria."));
                } else {
                    resultsSection.getChildren().clear();
                    for (Movie movie : searchResults) {
                        HBox movieLayout = new HBox(10);
                        Label movieLabel = new Label(movie.getTitle());
                        Button detailsButton = new Button("Details");
                        detailsButton.setOnAction(e -> showMovieDetails(movie));

                        Button addToFavoritesButton = new Button("Add to Favorites");
                        addToFavoritesButton.setOnAction(e -> addToFavorites(movie));

                        movieLayout.getChildren().addAll(movieLabel, detailsButton, addToFavoritesButton);
                        resultsSection.getChildren().add(movieLayout);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        favoritesButton.setOnAction(event -> showFavorites()); // Ajout de l'action pour afficher les favoris
        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(filtersSection, resultsSection);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("Movie Catalogue");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void showMovieDetails(Movie movie) {
        StringBuilder details = new StringBuilder();
        details.append("Title: ").append(movie.getTitle()).append("\n");
        details.append("ID: ").append(movie.getId()).append("\n");
        details.append("Release Date: ").append(movie.getRelease_date()).append("\n");
        details.append("Overview: ").append(movie.getOverview()).append("\n");
        details.append("Adult: ").append(movie.isAdult()).append("\n");
        details.append("Backdrop Path: ").append(movie.getBackdrop_path()).append("\n");
        details.append("Genre IDs: ").append(movie.getGenre_ids()).append("\n");
        details.append("Original Language: ").append(movie.getOriginal_language()).append("\n");
        details.append("Original Title: ").append(movie.getOriginal_title()).append("\n");
        details.append("Popularity: ").append(movie.getPopularity()).append("\n");
        details.append("Poster Path: ").append(movie.getPoster_path()).append("\n");
        details.append("Video: ").append(movie.isVideo()).append("\n");
        details.append("Vote Average: ").append(movie.getVote_average()).append("\n");
        details.append("Vote Count: ").append(movie.getVote_count()).append("\n");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Movie Details");
        alert.setHeaderText(null);
        alert.setContentText(details.toString());
        alert.showAndWait();
    }

    private void addToFavorites(Movie movie) {
        // Vérifie si le film est déjà présent dans la liste des favoris
        boolean isAlreadyFavorite = favoriteMovies.stream()
                .anyMatch(favorite -> favorite.getId() == movie.getId());

        if (!isAlreadyFavorite) {
            favoriteMovies.add(movie); // Ajoute le film à la liste des favoris seulement s'il n'est pas déjà présent
            System.out.println("Added to favorites: " + movie.getTitle());
        } else {
            System.out.println(movie.getTitle() + " is already in favorites.");
        }
    }


    private void showFavorites() {
        Stage favoritesStage = new Stage();
        VBox favoritesLayout = new VBox();
        favoritesLayout.setPadding(new Insets(10));

        for (Movie movie : new ArrayList<>(favoriteMovies)) {
            HBox favoriteLayout = new HBox(10);

            Label movieLabel = new Label(movie.getTitle());
            Button removeButton = new Button("Remove");
            removeButton.setOnAction(e -> {
                removeFromFavorites(movie);
                favoritesLayout.getChildren().remove(favoriteLayout);
            });

            favoritesLayout.getChildren().addAll(movieLabel, removeButton);
            favoritesLayout.getChildren().add(favoriteLayout);

        }
        Scene favoritesScene = new Scene(favoritesLayout, 400, 300);
        favoritesStage.setTitle("Favorites");
        favoritesStage.setScene(favoritesScene);
        favoritesStage.show();
    }

    private void removeFromFavorites(Movie movie) {
        favoriteMovies.remove(movie);
    }
    public static void main(String[] args) {
        launch(args);
    }
}

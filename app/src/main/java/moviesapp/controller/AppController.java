package moviesapp.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppController {

    @FXML
    private TextField titleField, startYearField, endYearField, ratingField;
    @FXML
    private FlowPane resultsSection;
    @FXML
    private Label sectionTitle;
    @FXML
    public static Favorites favoriteMovies = new Favorites(); // Liste pour stocker les films favoris
    @FXML
    private ComboBox<String> genreComboBox;

    private Map<String, Integer> genreMap = new HashMap<>();

    private Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }
    @FXML
    public void initialize() {
        displayMostViralMovies();
        fetchAndPopulateGenres();
    }

    private void fetchAndPopulateGenres() {
        Task<Map<Integer, String>> fetchGenresTask = new Task<>() {
            @Override
            protected Map<Integer, String> call() throws Exception {
                return TMDBApi.fetchMovieGenres();
            }
        };

        fetchGenresTask.setOnSucceeded(e -> {
            genreMap.clear();
            Platform.runLater(() -> {
                genreComboBox.getItems().clear();
                // Add "No Genre" option
                genreComboBox.getItems().add("No Genre");
            });
            for (Map.Entry<Integer, String> entry : fetchGenresTask.getValue().entrySet()) {
                genreMap.put(entry.getValue(), entry.getKey());
                Platform.runLater(() -> genreComboBox.getItems().add(entry.getValue()));

            }
        });

        new Thread(fetchGenresTask).start();
    }

    @FXML
    private void onSearch() {

        String title = titleField.getText().trim();
        String selectedGenre = genreComboBox.getSelectionModel().getSelectedItem();
        String startYear = startYearField.getText().trim();
        String endYear = endYearField.getText().trim();
        String rating = ratingField.getText().trim();

        String genreQueryParam = selectedGenre != null && !selectedGenre.equals("No Genre") ? genreMap.get(selectedGenre).toString() : "";


        try {
            sectionTitle.setText("-> Search Results");
            List<Movie> searchResults = TMDBApi.searchMovies(title, genreQueryParam, startYear, endYear, rating);
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

        if (favoriteMovies.getFavoriteMovies().isEmpty()) {
            resultsSection.getChildren().add(new Label("No favorites added."));
        } else {
            for (Movie movie : favoriteMovies.getFavoriteMovies()) {
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
        if (!favoriteMovies.getFavoriteMovies().contains(movie)) {
            movie.setFavorite(true);
            favoriteMovies.addFavoriteMovie(movie);
        }
    }

    private void removeFromFavorites(Movie movie) {
        if (favoriteMovies.getFavoriteMovies().contains(movie)) {
            movie.setFavorite(false);
            favoriteMovies.removeFavoriteMovie(movie);
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

            controller.setFavoritesActions(this::addToFavorites, this::removeFromFavorites, this::displayMovieDetails);

            resultsSection.getChildren().clear(); // Clear the tiles
            resultsSection.getChildren().add(detailsView); // Add the details view

            sectionTitle.setText("Movie Details"); // Update the section title accordingly

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onHome(ActionEvent event) {
        // Code pour revenir à la page d'accueil
        loadHomePage();
    }


    private void loadHomePage() {
        // Charger la vue des films les plus populaires
        displayMostViralMovies(); // Cette méthode devrait charger les films les plus populaires

        // Mettre à jour le titre de la section
        sectionTitle.setText("-> Most Popular Movies");
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        try {
            // Charger le fichier FXML de la page précédente
            FXMLLoader loader = new FXMLLoader(getClass().getResource("VotreFichierFXMLPrecedent.fxml"));
            Parent root = loader.load();

            // Obtenez la référence de la scène actuelle à partir de n'importe quel nœud de la scène
            Scene scene = ((Node) event.getSource()).getScene();

            // Configurez la nouvelle scène avec le contenu chargé à partir du fichier FXML précédent
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







}

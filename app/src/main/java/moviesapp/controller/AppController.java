package moviesapp.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import java.io.IOException;
import java.security.cert.PolicyNode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Contrôleur principal pour l'application MoviesApp.
 * Gère l'interface utilisateur et les interactions avec l'API TMDB pour rechercher et afficher des informations sur les films.
 */
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

    /**
     * Initialise le contrôleur. Cette méthode est automatiquement appelée après le chargement du fichier FXML.
     */
    @FXML
    public void initialize() {
        displayMostViralMovies();
        fetchAndPopulateGenres();

    }

    /**
     * Récupère les genres disponibles depuis l'API TMDB et les ajoute au ComboBox pour la sélection par l'utilisateur.
     */
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

    /**
     * Gère l'événement de recherche déclenché par l'utilisateur.
     * Utilise les critères fournis pour rechercher des films via l'API TMDB.
     */

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

    /**
     * Affiche les films marqués comme favoris par l'utilisateur.
     */
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

    /**
     * Ajoute un film à la liste des favoris de l'utilisateur.
     * @param movie Le film à ajouter.
     */
    public void addToFavorites(Movie movie) {
        if (!favoriteMovies.getFavoriteMovies().contains(movie)) {
            movie.setFavorite(true);
            favoriteMovies.addFavoriteMovie(movie);
        }
    }

    /**
     * Retire un film de la liste des favoris de l'utilisateur.
     * @param movie Le film à retirer.
     */
    private void removeFromFavorites(Movie movie) {
        if (favoriteMovies.getFavoriteMovies().contains(movie)) {
            movie.setFavorite(false);
            favoriteMovies.removeFavoriteMovie(movie);
        }
    }

    /**
     * Affiche les films les plus populaires à l'utilisateur.
     */
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

    /**
     * Affiche les détails d'un film spécifique.
     * @param movie Le film dont les détails doivent être affichés.
     */
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

    /**
     * Gère le retour à la vue principale (accueil) de l'application.
     * @param event L'événement déclencheur du retour à l'accueil.
     */
    @FXML
    private void onHome(ActionEvent event) {
        // Code pour revenir à la page d'accueil
        loadHomePage();
    }

    /**
     * Charge et affiche la vue de la page d'accueil, montrant généralement les films les plus populaires.
     */

    private void loadHomePage() {
        // Charger la vue des films les plus populaires
        displayMostViralMovies(); // Cette méthode devrait charger les films les plus populaires

        // Mettre à jour le titre de la section
        sectionTitle.setText("-> Most Popular Movies");
    }

    private void handleBackButton(ActionEvent event) {
        String previousPage = NavigationManager.getPreviousPage();
        if (previousPage != null) {
            // Code pour naviguer vers la page précédente
            // Vous pouvez utiliser FXMLLoader pour charger la vue de la page précédente
        } else {
            // Gérer le cas où il n'y a pas de page précédente
        }
    }
}

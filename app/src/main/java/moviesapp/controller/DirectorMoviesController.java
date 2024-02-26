package moviesapp.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import moviesapp.ApiManager.TMDBApi;
import java.io.IOException;
import java.util.List;

/**
 * Contrôleur pour gérer l'affichage des films d'un réalisateur spécifique.
 * Utilise TMDBApi pour récupérer les films par identifiant de réalisateur et les affiche dans un ListView.
 */
public class DirectorMoviesController {

    @FXML
    private ListView<String> moviesListView;

    private TMDBApi tmdbApi = new TMDBApi();

    /**
     * Charge et affiche les films d'un réalisateur spécifique.
     * @param directorId L'identifiant du réalisateur dont les films doivent être chargés.
     */
    public void loadMovies(String directorId) {
        new Thread(() -> {
            try {
                List<String> movieTitles = tmdbApi.getMoviesByDirector(directorId);
                Platform.runLater(() -> {
                    System.out.println("Affichage des films : " + movieTitles);
                    moviesListView.getItems().setAll(movieTitles);
                });
            } catch (IOException | InterruptedException e) {
                e.printStackTrace();
                Platform.runLater(() -> {
                    moviesListView.getItems().setAll("Impossible de charger les films");
                });
            }
        }).start();
    }
}

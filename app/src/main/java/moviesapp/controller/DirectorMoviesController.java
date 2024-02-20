package moviesapp.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;

import javafx.scene.control.ListView;
import moviesapp.ApiManager.TMDBApi;

import java.io.IOException;
import java.util.List;

public class DirectorMoviesController {



    @FXML
    private ListView<String> moviesListView;


    private TMDBApi tmdbApi = new TMDBApi();

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
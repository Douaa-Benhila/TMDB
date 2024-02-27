package moviesapp.controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Author;
import moviesapp.model.Movie;

import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

/**
 * Contrôleur pour gérer l'affichage des films d'un réalisateur spécifique.
 * Utilise TMDBApi pour récupérer les films par identifiant de réalisateur et les affiche dans un ListView.
 */
public class DirectorMoviesController {

    @FXML
    private ImageView directorImageView;
    @FXML private Label directorNameLabel;
    @FXML private Label directorBiographyLabel;
    @FXML
    private FlowPane filmDirector;

    @FXML
    private VBox moviesVBox;

    @FXML
    private ListView<String> moviesListView;

    private TMDBApi tmdbApi = new TMDBApi();

    /**
     * Charge et affiche les films d'un réalisateur spécifique.
     * @param directorId L'identifiant du réalisateur dont les films doivent être chargés.
     */

}

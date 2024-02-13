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

public class AppController  {

    @FXML
    private TextField titleField, genreField, startYearField, endYearField, ratingField;
    @FXML
    private FlowPane resultsSection;
    @FXML
    private Label sectionTitle; // Reference to the title label
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/MovieTile.fxml"));

    private List<Movie> favoriteMovies = new ArrayList<>(); // Liste pour stocker les films favoris







}

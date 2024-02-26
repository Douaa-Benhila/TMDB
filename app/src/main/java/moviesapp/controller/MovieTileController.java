package moviesapp.controller;

import com.sun.javafx.stage.EmbeddedWindow;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;


import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.EventObject;
import java.util.List;
import java.util.function.Consumer;


public class MovieTileController {
    @FXML
    private ImageView posterImage;
    @FXML
    private Label titleLabel;
    @FXML
    private Label yearLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Button addToFavoritesButton;
    @FXML
    private ImageView starImageView;
    public Movie movie ;
    private Consumer<Movie> onAddToFavorites;
    private Consumer<Movie> onRemoveFromFavorites;
    private Consumer<Movie> onDisplayDetails; // Add a new consumer for displaying details

    public void setMovie(Movie movie, Consumer<Movie> onAddToFavorites, Consumer<Movie> onRemoveFromFavorites, Consumer<Movie> onDisplayDetails){
        this.movie = movie;
        this.onAddToFavorites = onAddToFavorites;
        this.onRemoveFromFavorites = onRemoveFromFavorites;
        this.onDisplayDetails = onDisplayDetails;

        String imageUrl = TMDBApi.IMAGE_BASE_URL + (movie.getPoster_path() != null ? movie.getPoster_path() : "/img.webp");
        posterImage.setImage(new Image(imageUrl, true));
        titleLabel.setText(movie.getTitle());
        yearLabel.setText(movie.getRelease_date()); // Ensure this is formatted correctly
        ratingLabel.setText(String.format("       %.1f", movie.getVote_average()));
        Image starImage = new Image("/img2.png"); // Path to your star image
        starImageView.setImage(starImage);

        updateButtonAppearance();
        Node movieTile = null;
        movieTile.setOnMouseClicked(event -> onDisplayDetails.accept(movie));// Use the consumer to notify about the click

    }
    public void updateButtonAppearance() {
        if (AppController.favoriteMovies.getFavoriteMovies().contains(movie)) {
            addToFavoritesButton.setText("Liked");
            addToFavoritesButton.setStyle("-fx-background-color: #00ff00;");
        } else {
            addToFavoritesButton.setText("like");
            addToFavoritesButton.setStyle("");
        }
    }
    @FXML
    private void handleAddToFavorites() {
        if (AppController.favoriteMovies.getFavoriteMovies().contains(movie)) {
            onRemoveFromFavorites.accept(movie);
        } else {
            onAddToFavorites.accept(movie);
        }
        updateButtonAppearance();
    }

    private Scene previousScene;
    public void loadNewScene(String fxmlFile,ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            // Sauvegarder la scène précédente
            previousScene = scene;

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBackButtonAction(ActionEvent event) {
        if (previousScene != null) {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(previousScene);
            stage.show();
        } else {
            System.out.println("Aucune scène précédente à afficher.");
        }
    }






}


package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import moviesapp.model.Movie;


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

    private Movie movie ;
    private Consumer<Movie> onAddToFavorites;
    private Consumer<Movie> onRemoveFromFavorites;
    public void setMovie(Movie movie, Consumer<Movie> onAddToFavorites, Consumer<Movie> onRemoveFromFavorites) {
        this.movie = movie;
        titleLabel.setText(movie.getTitle());
        yearLabel.setText(movie.getRelease_date().toString()); // Adjust depending on how your date is formatted
        ratingLabel.setText(String.format("      %.1f", movie.getVote_average()));
    }
    public void updateButtonAppearance() {
        if (MovieCatalogueApp.favoriteMovies.contains(movie)) {
            addToFavoritesButton.setText("Liked");
            addToFavoritesButton.setStyle("-fx-background-color: #00ff00;");
        } else {
            addToFavoritesButton.setText("like");
            addToFavoritesButton.setStyle("");
        }
    }
    @FXML
    private void handleAddToFavorites() {
        if (MovieCatalogueApp.favoriteMovies.contains(movie)) {
            onRemoveFromFavorites.accept(movie);
        } else {
            onAddToFavorites.accept(movie);
        }
        updateButtonAppearance();
    }




}
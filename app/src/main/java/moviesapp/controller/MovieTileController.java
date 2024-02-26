package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;

import java.io.IOException;
import java.util.function.Consumer;

/**
 * Contrôleur pour la tuile de film dans l'application MoviesApp.
 * Cette classe gère l'affichage des informations d'un film spécifique dans une tuile.
 * Elle inclut le titre du film, l'année de sortie, le classement, ainsi qu'une image du poster.
 * Les utilisateurs peuvent ajouter ou retirer un film de leurs favoris directement depuis cette tuile.
 * Un clic sur la tuile ouvre une fenêtre détaillée avec plus d'informations sur le film.
 *
 * @see Movie - La classe du modèle représentant les données d'un film.
 */
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
    @FXML
    private VBox movieTile;
    public Movie movie ;
    private Consumer<Movie> onAddToFavorites;
    private Consumer<Movie> onRemoveFromFavorites;

    /**
     * Initialise la tuile avec les données d'un film et les callbacks pour les actions.
     *
     * @param movie Le film à afficher.
     * @param onAddToFavorites Callback à exécuter lors de l'ajout du film aux favoris.
     * @param onRemoveFromFavorites Callback à exécuter lors du retrait du film des favoris.
     * @param onDisplayDetails Callback à exécuter lors du clic sur la tuile pour afficher les détails du film.
     */
    public void setMovie(Movie movie, Consumer<Movie> onAddToFavorites, Consumer<Movie> onRemoveFromFavorites, Consumer<Movie> onDisplayDetails){
        this.movie = movie;
        this.onAddToFavorites = onAddToFavorites;
        this.onRemoveFromFavorites = onRemoveFromFavorites;

        String imageUrl = TMDBApi.IMAGE_BASE_URL + (movie.getPoster_path() != null ? movie.getPoster_path() : "/img.webp");
        posterImage.setImage(new Image(imageUrl, true));
        titleLabel.setText(movie.getTitle());
        yearLabel.setText(movie.getRelease_date());
        ratingLabel.setText(String.format("       %.1f", movie.getVote_average()));
        Image starImage = new Image("/img2.png");
        starImageView.setImage(starImage);

        updateButtonAppearance();
        movieTile.setOnMouseClicked(event -> onDisplayDetails.accept(movie));

    }

    /**
     * Met à jour l'apparence du bouton de favoris en fonction du statut de favori du film.
     */
    public void updateButtonAppearance() {
        if (AppController.favoriteMovies.getFavoriteMovies().contains(movie)) {
            addToFavoritesButton.setText("Liked");
            addToFavoritesButton.setStyle("-fx-background-color: #00ff00;");
        } else {
            addToFavoritesButton.setText("like");
            addToFavoritesButton.setStyle("");
        }
    }

    /**
     * Gère l'action d'ajouter ou de retirer un film des favoris.
     */
    @FXML
    private void handleAddToFavorites() {
        if (AppController.favoriteMovies.getFavoriteMovies().contains(movie)) {
            onRemoveFromFavorites.accept(movie);
        } else {
            onAddToFavorites.accept(movie);
        }
        updateButtonAppearance();
    }

    /**
     * Initialisation de la classe contrôleur. Configure l'action de clic sur la tuile.
     */
    public void initialize() {
        movieTile.setOnMouseClicked(event -> handleMovieTileClick());
    }

    /**
     * Gère le clic sur la tuile de film, ouvrant la fenêtre de détails du film.
     */
    private void handleMovieTileClick() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/MovieDetails.fxml"));
            Parent root = loader.load();
            MovieDetailsController controller = loader.getController();
            controller.setMovieDetails(movie);

            Stage detailsStage = new Stage();
            detailsStage.setTitle(movie.getTitle());
            detailsStage.setScene(new Scene(root));
            detailsStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

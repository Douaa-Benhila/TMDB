package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import moviesapp.model.Movie;

public class MovieDetailsController {

    @FXML private ImageView backdropImageView;
    @FXML private ImageView posterImageView;
    @FXML private Label titleLabel;
    @FXML private Label releaseDateLabel;
    @FXML private Label originalTitleLabel;
    @FXML private Label originalLanguageLabel;
    @FXML private Label genreLabel;
    @FXML private Text overviewText;
    @FXML private Label popularityLabel;
    @FXML private Label voteAverageLabel;
    @FXML private Label voteCountLabel;
    @FXML private Label adultLabel;
    @FXML private Label videoLabel;

    public void setMovieDetails(Movie movie) {
        String imagePath = "https://image.tmdb.org/t/p/w500"; // Base path for images from TMDB (adjust as necessary)
        backdropImageView.setImage(new Image(imagePath + movie.getBackdrop_path(), true));
        posterImageView.setImage(new Image(imagePath + movie.getPoster_path(), true));
        titleLabel.setText(movie.getTitle());
        releaseDateLabel.setText("Release Date: " + movie.getRelease_date());
        originalTitleLabel.setText("Original Title: " + movie.getOriginal_title());
        originalLanguageLabel.setText("Language: " + movie.getOriginal_language().toUpperCase());
        genreLabel.setText("Genres: " + movie.getGenre_ids().toString());
        overviewText.setText(movie.getOverview());
        popularityLabel.setText("Popularity: " + movie.getPopularity());
        voteAverageLabel.setText("Rating: " + movie.getVote_average() + " / 10");
        voteCountLabel.setText("Votes: " + movie.getVote_count());
        adultLabel.setText("Adult: " + (movie.isAdult() ? "Yes" : "No"));
        videoLabel.setText("Video: " + (movie.isVideo() ? "Yes" : "No"));
    }


}
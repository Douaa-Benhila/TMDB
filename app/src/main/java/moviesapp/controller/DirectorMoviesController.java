package moviesapp.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import moviesapp.model.Author;

public class DirectorMoviesController {

    @FXML
    private ImageView directorImageView;
    @FXML private Label directorNameLabel;
    @FXML private Label directorBiographyLabel;

    public void setDirectorDetails(Author author) {
        directorNameLabel.setText(author.getName());
        directorBiographyLabel.setText(author.getBiography());
        directorImageView.setImage(new Image(author.getImageUrl()));
    }
}
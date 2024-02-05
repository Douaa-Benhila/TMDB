package moviesapp;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.geometry.Insets;
import moviesapp.ApiManager.TMDBApi;

import java.io.IOException;
import java.util.List;

public class MovieCatalogueApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        VBox filtersSection = new VBox(10);
        filtersSection.setPadding(new Insets(10));
        TextField titleField = new TextField();
        titleField.setPromptText("Enter movie title");
        TextField genreField = new TextField();
        genreField.setPromptText("Enter genre");
        TextField yearField = new TextField();
        yearField.setPromptText("Enter year");
        TextField ratingField = new TextField();
        ratingField.setPromptText("Enter rating");
        Button searchButton = new Button("Search");
        filtersSection.getChildren().addAll(new Label("FILTERS:"), titleField, genreField, yearField, ratingField, searchButton);

        VBox resultsSection = new VBox(10);
        resultsSection.setPadding(new Insets(10));

        searchButton.setOnAction(event -> {
            String title = titleField.getText().trim();
            String genre = genreField.getText().trim();
            String year = yearField.getText().trim();
            String rating = ratingField.getText().trim();

            try {
                List<String> searchResults =TMDBApi.searchMoviesByFilters(title, genre, year, rating);

                if (searchResults.isEmpty()) {
                    resultsSection.getChildren().clear();
                    resultsSection.getChildren().add(new Label("No movies found with the given criteria."));
                } else {
                    resultsSection.getChildren().clear();
                    for (String result : searchResults) {
                        resultsSection.getChildren().add(new Label(result));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        HBox mainLayout = new HBox(20);
        mainLayout.getChildren().addAll(filtersSection, resultsSection);

        Scene scene = new Scene(mainLayout, 600, 400);
        primaryStage.setTitle("Movie Catalogue");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}



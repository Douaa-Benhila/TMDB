package moviesapp;

import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import moviesapp.model.TMDBApi;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        Favorites favorites = new Favorites();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Movie Organizer CLI!");

        while (true) {
            System.out.println("\nSelect an option:");
            System.out.println("1. Add a movie to favorites");
            System.out.println("2. Remove a movie from favorites");
            System.out.println("3. Show favorite movies");
            System.out.println("4. Exit");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    addMovieToFavorites(favorites);
                    break;
                case 2:
                    removeMovieFromFavorites(favorites);
                    break;
                case 3:
                    showFavoriteMovies(favorites);
                    break;
                case 4:
                    System.out.println("Exiting the Movie Organizer. Goodbye!");
                    scanner.close();
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }
        }
    }

    private static void addMovieToFavorites(Favorites favorites) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the title of the movie:");
        String title = scanner.nextLine();

        try {
            List<String> searchResults = TMDBApi.searchMovieTitles(title);

            if (searchResults.isEmpty()) {
                System.out.println("No movies found with the given title.");
            } else {
                System.out.println("Select a movie:");
                for (int i = 0; i < searchResults.size(); i++) {
                    System.out.println((i + 1) + ". " + searchResults.get(i));
                }

                int selectedOption = scanner.nextInt();

                if (selectedOption >= 1 && selectedOption <= searchResults.size()) {
                    String selectedMovieTitle = searchResults.get(selectedOption - 1);
                    Movie selectedMovie = TMDBApi.parseMovieFromSearchResult(TMDBApi.searchMovieByTitle(selectedMovieTitle));

                    favorites.addFavoriteMovie(selectedMovie);
                    System.out.println("Movie added to favorites: " + selectedMovieTitle);
                } else {
                    System.out.println("Invalid option selected.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void removeMovieFromFavorites(Favorites favorites) {
        Scanner scanner = new Scanner(System.in);

        List<Movie> favoriteMovies = favorites.getFavoriteMovies();

        if (favoriteMovies.isEmpty()) {
            System.out.println("No movies in favorites to remove.");
        } else {
            System.out.println("Select a movie to remove:");
            for (int i = 0; i < favoriteMovies.size(); i++) {
                System.out.println((i + 1) + ". " + favoriteMovies.get(i).getTitle());
            }

            int selectedOption = scanner.nextInt();

            if (selectedOption >= 1 && selectedOption <= favoriteMovies.size()) {
                Movie selectedMovie = favoriteMovies.get(selectedOption - 1);
                favorites.removeFavoriteMovie(selectedMovie);
                System.out.println("Movie removed from favorites: " + selectedMovie.getTitle());
            } else {
                System.out.println("Invalid option selected.");
            }
        }
    }


    private static void showFavoriteMovies(Favorites favorites) {
        favorites.showFavoritesMovies();
    }
}

package moviesapp;

import moviesapp.model.Favorites;
import moviesapp.model.Movie;

import java.util.List;
import java.util.Scanner;

public class MainApp {

    public static void main(String[] args) {
        Favorites favorites = new Favorites();
        Scanner scanner = new Scanner(System.in);

        Movie movie1 = new Movie(false, "/3KiwdVe0XqLNrBVYrpVMlvZaPdm.jpg", List.of(99), 32571, "xx", "Monkeyshines, No. 1", "Experimental film made to test the original cylinder format of the Kinet", 6.209, "/a41Z3Owp1TsXcUlBCRhbb9eJjWW.jpg", "1890-11-21",  "Monkeyshines, No. 1", false, 4.9, 90);

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
                    addMovieToFavorites(favorites, movie1);
                    break;
                case 2:
                    removeMovieFromFavorites(favorites, movie1);
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

}

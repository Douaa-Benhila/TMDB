package moviesapp;

import moviesapp.ApiManager.MovieSearchManager;
import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import java.util.Scanner;

/**
 * La classe AppCLI est responsable de l'interface utilisateur en ligne de commande
 * de l'application MoviesApp.
 */
public class AppCLI {
    private static final Scanner scanner = new Scanner(System.in);
    private static final Favorites favorites = new Favorites();
    private static final MovieSearchManager searchManager = new MovieSearchManager();

    /**
     * Affiche le menu des options disponibles.
     */
    public static void main(String[] args) {
        System.out.println("Bienvenue dans l'application MoviesApp!");

        boolean running = true;
        while (running) {
            System.out.println("\nOptions:");
            System.out.println("1. Rechercher des films par titre");
            System.out.println("2. Rechercher un film par filtres");
            System.out.println("3. Afficher les favoris");
            System.out.println("4. Ajouter un film aux favoris par titre");
            System.out.println("5. Supprimer un film des favoris par titre");
            System.out.println("6. Quitter");
            System.out.print("Choisissez une option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline left-over
/**
 * Méthode principale pour lancer l'application.
 * @param args Les arguments de ligne de commande (non utilisés).
 */
            switch (choice) {
                case 1:
                    searchManager.searchByTitle();
                    break;
                case 2:
                    searchManager.searchByFilters();
                    break;

                case 3:
                    displayFavorites();
                    break;
                case 4:
                    addFavoriteByTitle();
                    break;
                case 5:
                    removeFavoriteByTitle();
                    break;
                case 6:
                    running = false;
                    break;
                default:
                    System.out.println("Option non valide, veuillez réessayer.");
            }
        }
        System.out.println("Merci d'avoir utilisé l'application MoviesApp!");
    }

    private static void displayFavorites() {
        favorites.showFavoritesMovies();
    }

    private static void addFavoriteByTitle() {
        System.out.print("Entrez le titre du film à ajouter aux favoris : ");
        String title = scanner.nextLine();
        searchManager.addFavoriteByTitle(title, favorites);
    }

    private static void removeFavoriteByTitle() {
        System.out.print("Entrez le titre du film à retirer des favoris : ");
        String title = scanner.nextLine();
        favorites.removeFavoriteByTitle(title);
    }
}

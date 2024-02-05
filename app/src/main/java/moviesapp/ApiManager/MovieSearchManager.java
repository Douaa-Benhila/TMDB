package moviesapp.ApiManager;

import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import java.io.IOException;
import java.util.Scanner;

/**
 * La classe MovieSearchManager facilite la recherche de films via l'API TMDB.
 * Elle permet aux utilisateurs de rechercher des films par titre ou par filtres avancés.
 */
public class MovieSearchManager {
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Affiche les options de recherche de films et gère le choix de l'utilisateur.
     */
    public void searchMovies() {
        System.out.println("Choisissez le type de recherche :");
        System.out.println("1. Recherche par nom");
        System.out.println("2. Recherche avancée par filtres");
        System.out.print("Votre choix : ");
        int choix = scanner.nextInt();
        scanner.nextLine(); // Nettoyer le buffer

        switch (choix) {
            case 1:
                searchByTitle();
                break;
            case 2:
                searchByFilters();
                break;
            default:
                System.out.println("Option non valide, réessayez.");
                break;
        }
    }

    /**
     * Effectue une recherche de film par titre.
     * Demande à l'utilisateur de saisir un titre et affiche les résultats correspondants.
     */
    private void searchByTitle() {
        System.out.print("Entrez le titre du film à rechercher : ");
        String title = scanner.nextLine();
        try {
            String searchResult = TMDBApi.searchMovieByTitle(title);
            MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);
            if (movieListResponse != null && movieListResponse.getResults() != null && !movieListResponse.getResults().isEmpty()) {
                System.out.println("--------> Résultats de la recherche : ");
                for (Movie movie : movieListResponse.getResults()) {
                    System.out.println(movie.toString());
                }
            } else {
                System.out.println("Aucun film trouvé.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du film : " + e.getMessage());
        }
    }

    /**
     * Permet à l'utilisateur d'effectuer une recherche avancée de films en spécifiant plusieurs filtres.
     */
    private void searchByFilters() {
        System.out.println("Entrez les critères de recherche (laissez vide pour ignorer) :");
        // Collecte les critères de recherche
        System.out.print("Titre (laissez vide pour ignorer) : ");
        String title = scanner.nextLine();
        System.out.print("Année de sortie (laissez vide pour ignorer) : ");
        String year = scanner.nextLine();
        System.out.print("Note minimale (0-10, laissez vide pour ignorer) : ");
        String voteAverage = scanner.nextLine();
        System.out.print("Genre (ID, laissez vide pour ignorer) : ");
        String genreId = scanner.nextLine();

        try {
            // Construit la requête et affiche les résultats
            String searchResult = discoverMoviesWithFilters(title, year, voteAverage, genreId);
            MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);
            if (movieListResponse != null && movieListResponse.getResults() != null && !movieListResponse.getResults().isEmpty()) {
                System.out.println("Résultats de la recherche : ");
                for (Movie movie : movieListResponse.getResults()) {
                    System.out.println(movie.toString());
                }
            } else {
                System.out.println("Aucun film trouvé avec ces filtres.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche des films : " + e.getMessage());
        }
    }

    /**
     * Construit une requête pour la recherche de films avec des filtres spécifiques.
     *
     * @param title Le titre du film (optionnel).
     * @param year L'année de sortie du film (optionnel).
     * @param voteAverage La note minimale du film (optionnel).
     * @param genreId L'ID du genre du film (optionnel).
     * @return Les résultats de la recherche sous forme de chaîne JSON.
     * @throws IOException Si une erreur d'entrée/sortie se produit.
     */
    private String discoverMoviesWithFilters(String title, String year, String voteAverage, String genreId) throws IOException {
        StringBuilder queryParams = new StringBuilder();

        // Ajoute les filtres à la requête
        if (!year.isEmpty()) queryParams.append("&primary_release_year=").append(year);
        if (!voteAverage.isEmpty()) queryParams.append("&vote_average.gte=").append(voteAverage);
        if (!genreId.isEmpty()) queryParams.append("&with_genres=").append(genreId);

        return TMDBApi.discoverMovies(queryParams.toString());
    }

    /**
     * Ajoute un film aux favoris par titre après recherche.
     *
     * @param title Le titre du film à ajouter aux favoris.
     * @param favorites L'instance de la classe Favorites pour gérer la liste des favoris.
     */
    public void addFavoriteByTitle(String title, Favorites favorites) {
        try {
            // Recherche le film par titre et l'ajoute aux favoris si trouvé
            String searchResult = TMDBApi.searchMovieByTitle(title);
            Movie movie = TMDBApi.parseMovieFromSearchResult(searchResult);
            if (movie != null) {
                favorites.addFavoriteMovie(movie);
                System.out.println(movie.getTitle() + " a été ajouté aux favoris.");
            } else {
                System.out.println("Film non trouvé.");
            }
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du film : " + e.getMessage());
        }
    }
}

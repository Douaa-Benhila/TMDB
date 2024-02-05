package moviesapp.ApiManager;

import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;

import java.io.IOException;
import java.util.Scanner;

public class MovieSearchManager {
    private static final Scanner scanner = new Scanner(System.in);

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

    private void searchByTitle() {
        System.out.print("Entrez le titre du film à rechercher : ");
        String title = scanner.nextLine();
        try {
            String searchResult = TMDBApi.searchMovieByTitle(title);
            MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);
            if (movieListResponse != null && movieListResponse.getResults() != null && !movieListResponse.getResults().isEmpty()) {
                System.out.println("Résultats de la recherche : ");
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

    private void searchByFilters() {
        System.out.println("Entrez les critères de recherche (laissez vide pour ignorer) :");

        System.out.print("Titre (laissez vide pour ignorer) : ");
        String title = scanner.nextLine();

        System.out.print("Année de sortie (laissez vide pour ignorer) : ");
        String year = scanner.nextLine();

        System.out.print("Note minimale (0-10, laissez vide pour ignorer) : ");
        String voteAverage = scanner.nextLine();

        System.out.print("Genre (ID, laissez vide pour ignorer) : ");
        String genreId = scanner.nextLine();

        try {
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

    private String discoverMoviesWithFilters(String title, String year, String voteAverage, String genreId) throws IOException {
        StringBuilder queryParams = new StringBuilder();

        if (!title.isEmpty()) {
            // Pour la recherche par titre, vous devriez utiliser "/search/movie"
            return TMDBApi.searchMovieByTitle(title);
        }

        if (!year.isEmpty()) {
            queryParams.append("&primary_release_year=").append(year);
        }
        if (!voteAverage.isEmpty()) {
            queryParams.append("&vote_average.gte=").append(voteAverage);
        }
        if (!genreId.isEmpty()) {
            queryParams.append("&with_genres=").append(genreId);
        }

        return TMDBApi.discoverMovies(queryParams.toString());
    }

    public void addFavoriteByTitle(String title, Favorites favorites) {
        try {
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

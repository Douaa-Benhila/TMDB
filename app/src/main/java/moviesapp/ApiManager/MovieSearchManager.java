package moviesapp.ApiManager;

import moviesapp.model.Favorites;
import moviesapp.model.Movie;
import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;

import java.io.IOException;
import java.util.Scanner;

/**
 * La classe MovieSearchManager est responsable de la recherche de films dans l'API TMDB
 * en utilisant soit une recherche par titre, soit une recherche avancée par filtres.
 */
public class MovieSearchManager {
    private static final Scanner scanner = new Scanner(System.in);
    /**
     * Méthode permettant de démarrer le processus de recherche de films.
     * L'utilisateur est invité à choisir entre une recherche par titre ou une recherche avancée par filtres.
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
     * Méthode permettant de démarrer le processus de recherche de films.
     * L'utilisateur est invité à choisir entre une recherche par titre ou une recherche avancée par filtres.
     */
    private void searchByTitle() {
        System.out.print("Entrez le titre du film à rechercher : ");
        String title = validateInput(scanner.nextLine());
        try {
            String searchResult = TMDBApi.searchMovieByTitle(title);
            handleSearchResult(searchResult);
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du film : " + e.getMessage());
        }
    }
    /**
     * Effectue une recherche de films avancée par filtres.
     * L'utilisateur saisit les critères de recherche facultatifs.
     */
    private void searchByFilters() {
        System.out.println("Entrez les critères de recherche (laissez vide pour ignorer) :");

        String title = getTitle();
        String year = getYear();
        String voteAverage = getVoteAverage();
        String language = getLanguage();
        String minDuration = getMinDuration();
        String contentType = getContentType();
        String minPopularity = getMinPopularity();
        String actors = getActors();
        String directors = getDirectors();

        try {
            String searchResult = discoverMoviesWithFilters(title, year, voteAverage, language, minDuration, contentType, minPopularity, actors, directors);
            handleSearchResult(searchResult);
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche des films : " + e.getMessage());
        }
    }

    private String getTitle() {
        System.out.print("Titre (laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getYear() {
        System.out.print("Année de sortie (laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getVoteAverage() {
        System.out.print("Note minimale (0-10, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getLanguage() {
        System.out.print("Langue (code ISO 639-1, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getMinDuration() {
        System.out.print("Durée minimale (en minutes, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getContentType() {
        System.out.print("Type de contenu (movie, series, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getMinPopularity() {
        System.out.print("Popularité minimale (0-100, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getActors() {
        System.out.print("Acteurs (séparés par des virgules, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String getDirectors() {
        System.out.print("Réalisateurs (séparés par des virgules, laissez vide pour ignorer) : ");
        return validateInput(scanner.nextLine());
    }

    private String validateInput(String input) {
        // Validez l'entrée ici selon vos besoins
        return input.trim();
    }

    private void handleSearchResult(String searchResult) {
        MovieListResponse movieListResponse = JsonParser.parseMovieList(searchResult);
        if (movieListResponse != null && movieListResponse.getResults() != null && !movieListResponse.getResults().isEmpty()) {
            System.out.println("Résultats de la recherche : ");
            for (Movie movie : movieListResponse.getResults()) {
                System.out.println(movie.toString());
            }
        } else {
            System.out.println("Aucun film trouvé.");
        }
    }

    private String discoverMoviesWithFilters(String title, String year, String voteAverage, String language, String minDuration, String contentType, String minPopularity, String actors, String directors) throws IOException {
        // Construction de la requête en fonction des filtres spécifiés
        StringBuilder queryParams = new StringBuilder();

        if (!title.isEmpty()) {
            return TMDBApi.searchMovieByTitle(title);
        }

        if (!year.isEmpty()) {
            queryParams.append("&primary_release_year=").append(year);
        }
        if (!voteAverage.isEmpty()) {
            queryParams.append("&vote_average.gte=").append(voteAverage);
        }
        if (!language.isEmpty()) {
            queryParams.append("&language=").append(language);
        }
        if (!minDuration.isEmpty()) {
            queryParams.append("&with_runtime.gte=").append(minDuration);
        }
        if (!contentType.isEmpty()) {
            queryParams.append("&type=").append(contentType);
        }
        if (!minPopularity.isEmpty()) {
            queryParams.append("&popularity.gte=").append(minPopularity);
        }
        if (!actors.isEmpty()) {
            queryParams.append("&with_cast=").append(actors);
        }
        if (!directors.isEmpty()) {
            queryParams.append("&with_crew=").append(directors);
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

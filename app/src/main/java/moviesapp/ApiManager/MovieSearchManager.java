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
    private String title;
    private String year;
    private String voteAverage;
    private String language;
    private String minDuration;
    private String contentType;
    private String minPopularity;
    private String actors;
    private String directors;

    public MovieSearchManager() {
        // Initialisez les filtres à des valeurs par défaut ou à des chaînes vides si nécessaire
        this.title = "";
        this.year = "";
        this.voteAverage = "";
        this.language = "";
        this.minDuration = "";
        this.contentType = "";
        this.minPopularity = "";
        this.actors = "";
        this.directors = "";
    }
    public void searchByTitle() {
        System.out.print("Entrez le titre du film à rechercher : ");
        this.title = scanner.nextLine();
        try {
            String searchResult = TMDBApi.searchMovieByTitle(this.title);
            handleSearchResult(searchResult);
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche du film : " + e.getMessage());
        }
    }
    /**
     * Effectue une recherche de films avancée par filtres.
     * L'utilisateur saisit les critères de recherche facultatifs.
     */
    public void searchByFilters() {
        // Demandez à l'utilisateur de saisir les filtres et stockez-les dans les variables membres
        System.out.println("Entrez les critères de recherche (laissez vide pour ignorer) :");
        this.title = getTitle();
        this.year = getYear();
        this.voteAverage = getVoteAverage();
        this.language = getLanguage();
        this.minDuration = getMinDuration();
        this.contentType = getContentType();
        this.minPopularity = getMinPopularity();
        this.actors = getActors();
        this.directors = getDirectors();

        // Effectuez la recherche en utilisant les filtres spécifiés
        try {
            String searchResult = discoverMoviesWithFilters();
            handleSearchResult(searchResult);
        } catch (IOException e) {
            System.err.println("Erreur lors de la recherche des films : " + e.getMessage());
        }
    }
    private String getTitle() {
        // Retourne une chaîne vide pour indiquer que l'utilisateur a choisi de ne pas saisir de titre
        return "";
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

    /**
     * Traite le résultat d'une recherche de films et affiche les films trouvés ou un message indiquant qu'aucun film n'a été trouvé.
     * Cette méthode analyse d'abord la chaîne de caractères JSON représentant le résultat de la recherche en utilisant un parseur JSON spécifique
     * pour obtenir une instance de `MovieListResponse`. Si des films sont trouvés, elle itère sur la liste des films et les affiche.
     * Sinon, elle informe l'utilisateur qu'aucun film n'a été trouvé.
     *
     * @param searchResult Le résultat de la recherche sous forme de chaîne de caractères JSON à analyser.
     */
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

    /**
     * Découvre des films en utilisant divers filtres spécifiés par l'utilisateur et retourne le résultat sous forme de chaîne de caractères JSON.
     * Cette méthode construit dynamiquement les paramètres de requête en fonction des filtres fournis, tels que l'année de sortie,
     * la note moyenne, la langue, la durée minimale, le type de contenu, la popularité minimale, et les identifiants des acteurs et des réalisateurs.
     * Si le titre n'est pas vide, elle effectue une recherche directe par titre à la place. La méthode retourne ensuite le résultat de la découverte
     * ou de la recherche de films en fonction des paramètres de requête construits.
     *
     * @return Le résultat de la découverte de films sous forme de chaîne de caractères JSON.
     * @throws IOException Si une erreur d'entrée/sortie se produit pendant l'exécution de la requête.
     */
    private String discoverMoviesWithFilters() throws IOException {
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

    /**
     * Ajoute un film aux favoris en recherchant d'abord le film par son titre. Cette méthode utilise l'API pour rechercher le film
     * par son titre et, si trouvé, l'ajoute à la liste des favoris spécifiée. Elle informe l'utilisateur si le film a été ajouté avec succès
     * aux favoris ou si le film n'a pas été trouvé.
     *
     * @param title Le titre du film à ajouter aux favoris.
     * @param favorites L'instance de `Favorites` où le film sera ajouté si trouvé.
     */
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

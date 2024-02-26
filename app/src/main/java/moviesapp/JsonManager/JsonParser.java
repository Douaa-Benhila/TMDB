package moviesapp.JsonManager;

import com.google.gson.Gson;

/**
 * Classe utilitaire pour la manipulation de données JSON dans l'application MoviesApp.
 * Cette classe fournit des méthodes statiques pour convertir des chaînes JSON en objets Java.
 * Elle utilise la bibliothèque Gson pour effectuer la sérialisation et la désérialisation
 * des objets. Son utilisation principale est de faciliter la conversion des réponses JSON
 * obtenues des APIs en objets Java utilisables par l'application.
 */
public class JsonParser {

    /**
     * Convertit une chaîne JSON en un objet de type MovieListResponse.
     * Cette méthode prend une chaîne JSON représentant une liste de films, telle qu'elle pourrait
     * être obtenue d'une API comme TMDB, et la convertit en une instance de MovieListResponse,
     * qui contient une liste des objets Movie.
     *
     * @param json La chaîne JSON à convertir.
     * @return Un objet MovieListResponse représentant les données JSON converties.
     */
    public static MovieListResponse parseMovieList(String json) { //responsable de la conversion d'une chaîne JSON en un objet de type MovieListResponse.
        Gson gson = new Gson();
        return gson.fromJson(json, MovieListResponse.class);
    }
}
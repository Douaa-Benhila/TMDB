package moviesapp;

import moviesapp.model.JsonParser;
import moviesapp.model.Movie;
import moviesapp.model.MovieListResponse;

import java.io.IOException;
import java.util.Scanner;

import static moviesapp.model.TMDBApi.sendGET;

public class AppCLI {
    public static void main(String[] args) {
        try {
            String popularMoviesJson = sendGET("/movie/top_rated", "");

            System.out.println("Réponse JSON pour les films les plus notés :");
            System.out.println(popularMoviesJson);

            /////////////////////////////////////////////

            /*
            * Pour utiliser l'API de TMDb dans votre projet Java, vous pouvez exploiter différents endpoints selon
            les données que vous souhaitez obtenir. Voici quelques exemples d'endpoints utiles :

            -> Films Populaires: /movie/popular - Renvoie une liste de films populaires.
            -> Films Mieux Notés: /movie/top_rated - Renvoie une liste des films les mieux notés.
            -> Films à Venir: /movie/upcoming - Renvoie une liste de films à venir.
            -> Détails d'un Film: /movie/{movie_id} - Renvoie les détails d'un film spécifique, où {movie_id} est l'ID du film.
            -> Recherche de Films: /search/movie - Permet de rechercher des films en fonction d'un terme de recherche.
            Pour utiliser les queryParams (paramètres de requête), vous pouvez les ajouter à la méthode sendGET sous
            forme de chaîne de caractères. Par exemple, si vous voulez rechercher des films avec un mot-clé spécifique,
            vous pouvez utiliser l'endpoint /search/movie avec un queryParams comme &query=Inception. Voici comment cela
            pourrait être intégré dans votre code :

            *  */
            /*String keyword = "Inception";
            String searchResultsJson = sendGET("/search/movie", "&query=" + keyword);

            System.out.println("Réponse JSON pour la recherche de films avec le mot-clé '" + keyword + "' :");
            System.out.println(searchResultsJson);*/

            String json = popularMoviesJson;
            MovieListResponse movieListResponse = JsonParser.parseMovieList(json);
            System.out.println("***************************************\n");
            System.out.println("-------> le nombre total de résultats est :" + movieListResponse.getTotalResults() + "\n");
            for (Movie movie : movieListResponse.getResults()) {
                System.out.println(movie.toString());
                System.out.println("---------------------------\n");
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

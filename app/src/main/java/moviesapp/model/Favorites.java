package moviesapp.model;

import javafx.scene.control.TableColumn;
import moviesapp.ApiManager.TMDBApi;
import moviesapp.model.Movie;

import java.util.ArrayList;
import java.util.List;
/**
 * Gère une liste de films favoris. Fournit des fonctionnalités pour ajouter, supprimer et afficher des films dans la liste des favoris.
 */
public class Favorites {
    private List<Movie> favoriteMovies;
    /**
     * Construit une nouvelle instance de Favoris.
     * Initialise une liste vide de films favoris.
     */
    public Favorites() {
        favoriteMovies = new ArrayList<>();
    }

    /**
     * Retourne la liste de films favoris.
     *
     * @return Une liste de films marqués comme favoris.
     */
    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    /**
     * Ajoute un film à la liste des favoris.
     *
     * @param movie Le film à ajouter à la liste des favoris.
     */
    public void addFavoriteMovie(Movie movie) {
        favoriteMovies.add(movie);
    }

    /**
     * Supprime un film de la liste des favoris.
     *
     * @param movie Le film à supprimer de la liste des favoris.
     */
    public void removeFavoriteMovie(Movie movie) {
        favoriteMovies.remove(movie);
    }

    /**
     * Affiche la liste des films favoris dans la console.
     * Si la liste est vide, un message indiquant qu'il n'y a pas de films favoris est affiché.
     */
    public void showFavoritesMovies(){
        if(favoriteMovies.isEmpty()){
            System.out.println("Il n'y a aucun film favori.");
        }
        else {
            System.out.println("Liste des films favoris :");
            for (Movie movie : favoriteMovies){
                System.out.println(movie);
            }
        }
    }

    public Movie getFavoriteMovieByTitle(String title) {
        for (Movie movie : favoriteMovies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null; // Film non trouvé
    }

    public void removeFavoriteByTitle(String title) {
        Movie movie = getFavoriteMovieByTitle(title);
        if (movie != null) {
            removeFavoriteMovie(movie);
            System.out.println(movie.getTitle() + " a été retiré des favoris.");
        } else {
            System.out.println("Film non trouvé dans les favoris.");
        }
    }
}

package moviesapp.model;

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

    /**
     * Recherche un film par son titre dans la liste des films favoris.
     * Cette méthode parcourt la liste des films favoris et retourne le premier film dont le titre
     * correspond exactement (sans tenir compte de la casse) au titre fourni en paramètre. Cela permet
     * de retrouver facilement un film spécifique parmi les favoris en utilisant son titre comme critère
     * de recherche.
     *
     * @param title Le titre du film à rechercher parmi les favoris.
     * @return Le film correspondant au titre fourni, ou {@code null} si aucun film correspondant n'est trouvé.
     */
    public Movie getFavoriteMovieByTitle(String title) {
        for (Movie movie : favoriteMovies) {
            if (movie.getTitle().equalsIgnoreCase(title)) {
                return movie;
            }
        }
        return null; // Film non trouvé
    }

    /**
     * Retire un film des favoris en utilisant son titre comme critère de recherche.
     * Cette méthode utilise {@link #getFavoriteMovieByTitle(String)} pour trouver le film spécifique
     * par son titre. Si le film est trouvé dans la liste des favoris, il est retiré de cette liste.
     * Un message est affiché pour confirmer la suppression du film des favoris ou pour informer
     * l'utilisateur si le film n'est pas trouvé dans la liste des favoris.
     *
     * @param title Le titre du film à retirer des favoris.
     */
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

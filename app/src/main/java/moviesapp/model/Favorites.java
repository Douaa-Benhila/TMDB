package moviesapp.model;

import javafx.scene.control.TableColumn;
import moviesapp.model.Movie;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
/**
 * Manages a list of favorite movies. Provides functionality to add, remove, and display movies in the favorites list.
 */
public class Favorites {
    private List<Movie> favoriteMovies;
    /**
     * Constructs a new Favorites instance.
     * Initializes an empty list of favorite movies.
     */
    public Favorites() {
        favoriteMovies = new ArrayList<>();
    }

    /**
     * Returns the list of favorite movies.
     *
     * @return A list of movies marked as favorites.
     */
    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    /**
     * Adds a movie to the list of favorites.
     *
     * @param movie The movie to be added to the favorites list.
     */
    public void addFavoriteMovie(Movie movie) {
        favoriteMovies.add(movie);
    }

    /**
     * Removes a movie from the list of favorites.
     *
     * @param movie The movie to be removed from the favorites list.
     */
    public void removeFavoriteMovie(Movie movie) {
        favoriteMovies.remove(movie);
    }

    /**
     * Displays the list of favorite movies in the console.
     * If the list is empty, a message indicating no favorite movies is shown.
     */
    public void showFavoritesMovies(){
        if(favoriteMovies.isEmpty()){
            System.out.println("There is no favorite movie.");
        }
        else {
            System.out.println("Favorites movies list :");
            for (Movie movie : favoriteMovies){
                System.out.println(movie);
            }
        }
    }
}


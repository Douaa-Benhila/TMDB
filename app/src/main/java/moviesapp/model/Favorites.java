package moviesapp.model;

import javafx.scene.control.TableColumn;
import moviesapp.model.Movie;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;

public class Favorites {
    private List<Movie> favoriteMovies;

    public Favorites() {
        favoriteMovies = new ArrayList<>();
    }

    public List<Movie> getFavoriteMovies() {
        return favoriteMovies;
    }

    public void addFavoriteMovie(Movie movie) {
        favoriteMovies.add(movie);
    }

    public void removeFavoriteMovie(Movie movie) {
        favoriteMovies.remove(movie);
    }

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


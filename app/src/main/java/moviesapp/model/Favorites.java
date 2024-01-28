package moviesapp.model;

import moviesapp.model.Movie;

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

}


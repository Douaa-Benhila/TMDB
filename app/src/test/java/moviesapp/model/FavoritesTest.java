package moviesapp.model;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;

public class FavoritesTest {

    @Test
    public void testAddAndRemoveFavoriteMovie() {
        Favorites favorites = new Favorites();
        Movie movie1 = new Movie(true, "/backdrop1.jpg", null, 1, "en", "Title 1", "Overview 1", 5.0, "/poster1.jpg", "2022-01-01", "Movie 1", false, 4.5, 50);
        Movie movie2 = new Movie(false, "/backdrop2.jpg", null, 2, "fr", "Title 2", "Overview 2", 7.5, "/poster2.jpg", "2022-02-01", "Movie 2", true, 8.0, 100);

        favorites.addFavoriteMovie(movie1);
        favorites.addFavoriteMovie(movie2);

        assertEquals(2, favorites.getFavoriteMovies().size());
        assertTrue(favorites.getFavoriteMovies().contains(movie1));
        assertTrue(favorites.getFavoriteMovies().contains(movie2));

        favorites.removeFavoriteMovie(movie1);

        assertEquals(1, favorites.getFavoriteMovies().size());
        assertFalse(favorites.getFavoriteMovies().contains(movie1));
        assertTrue(favorites.getFavoriteMovies().contains(movie2));
    }

    @Test
    public void testShowFavoritesMovies() {
        Favorites favorites = new Favorites();
        Movie movie1 = new Movie(true, "/backdrop1.jpg", null, 1, "en", "Title 1", "Overview 1", 5.0, "/poster1.jpg", "2022-01-01", "Movie 1", false, 4.5, 50);
        Movie movie2 = new Movie(false, "/backdrop2.jpg", null, 2, "fr", "Title 2", "Overview 2", 7.5, "/poster2.jpg", "2022-02-01", "Movie 2", true, 8.0, 100);

        favorites.addFavoriteMovie(movie1);
        favorites.addFavoriteMovie(movie2);

        // Capture la sortie console pour vérifier les messages
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        favorites.showFavoritesMovies();

        String expectedOutput = "Favorites movies list :\n" +
                "Movie{adult=true, backdropPath='/backdrop1.jpg', genreIds=null, id=1, originalLanguage='en', originalTitle='Title 1', overview='Overview 1', popularity=5.0, posterPath='/poster1.jpg', releaseDate='2022-01-01', title='Movie 1', video=false, voteAverage=4.5, voteCount=50}\n" +
                "Movie{adult=false, backdropPath='/backdrop2.jpg', genreIds=null, id=2, originalLanguage='fr', originalTitle='Title 2', overview='Overview 2', popularity=7.5, posterPath='/poster2.jpg', releaseDate='2022-02-01', title='Movie 2', video=true, voteAverage=8.0, voteCount=100}\n";

        assertEquals(expectedOutput, outputStream.toString());
    }
}
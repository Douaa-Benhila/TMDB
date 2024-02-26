package moviesapp.model;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Locale;


import static moviesapp.AppCLI.favorites;
import static org.junit.jupiter.api.Assertions.*;

public class FavoritesTest {

    private final PrintStream standardOut = System.out;
    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

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

    @BeforeEach
    public void setUp() {
        System.setOut(new PrintStream(outputStream));
        favorites = new Favorites();
    }

    @AfterEach
    public void tearDown() {
        System.setOut(standardOut);
    }

    @Test
    public void testShowFavoritesMovies() {
        Favorites favorites = new Favorites();
        Movie movie1 = new Movie(true, "/backdrop1.jpg", Arrays.asList(28, 12), 1, "en", "Title 1", "This is an overview.", 8.5, "/poster1.jpg", "2022-01-01", "Movie 1", false, 9.0, 100);
        Movie movie2 = new Movie(false, "/backdrop2.jpg", Arrays.asList(35, 14), 2, "fr", "Title 2", "Ceci est un résumé.", 7.5, "/poster2.jpg", "2022-02-02", "Movie 2", true, 8.5, 200);

        favorites.addFavoriteMovie(movie1);
        favorites.addFavoriteMovie(movie2);

        // Capture la sortie console
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));

        favorites.showFavoritesMovies();

        // Restaure la sortie standard
        System.setOut(originalOut);

        String output = outputStream.toString().trim();

        // Validation partielle pour éviter les problèmes de formatage
        assertTrue(output.contains("Movie 1"));
        assertTrue(output.contains("2022-01-01"));
        assertTrue(output.contains("EN"));
        assertTrue(output.contains("9,0")); // Utilisez le séparateur décimal correct selon votre locale
        assertTrue(output.contains("Movie 2"));
        assertTrue(output.contains("2022-02-02"));
        assertTrue(output.contains("FR"));
        assertTrue(output.contains("8,5")); // Utilisez le séparateur décimal correct selon votre locale

        // Assurez-vous que les genres et les popularités sont mentionnés correctement
        assertTrue(output.contains("[28, 12]"));
        assertTrue(output.contains("[35, 14]"));
        assertTrue(output.contains("8,5")); // Popularité de Movie 1
        assertTrue(output.contains("7,5")); // Popularité de Movie 2
    }
}
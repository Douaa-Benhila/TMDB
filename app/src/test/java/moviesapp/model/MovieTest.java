package moviesapp.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MovieTest {

    @Test
    public void testMovieCreationAndGetters() {

        Movie movie = new Movie(
                false,
                "/backdrop.jpg",
                Arrays.asList(1, 2, 3),
                123,
                "en",
                "Original Title",
                "Overview",
                7.5,
                "/poster.jpg",
                "2022-01-01",
                "Test Movie",
                false,
                7.2,
                100
        );


        assertEquals(false, movie.isAdult());
        assertEquals("/backdrop.jpg", movie.getBackdrop_path());
        assertEquals(Arrays.asList(1, 2, 3), movie.getGenre_ids());
        assertEquals(123, movie.getId());
        assertEquals("en", movie.getOriginal_language());
        assertEquals("Original Title", movie.getOriginal_title());
        assertEquals("Overview", movie.getOverview());
        assertEquals(7.5, movie.getPopularity(), 0.001);
        assertEquals("/poster.jpg", movie.getPoster_path());
        assertEquals("2022-01-01", movie.getRelease_date());
        assertEquals("Test Movie", movie.getTitle());
        assertEquals(false, movie.isVideo());
        assertEquals(7.2, movie.getVote_average(), 0.001);
        assertEquals(100, movie.getVote_count());
    }

    @Test
    public void testMovieToString() {
        Movie movie = new Movie(
                false,
                "/backdrop.jpg",
                Arrays.asList(1, 2, 3),
                123,
                "en",
                "Original Title",
                "Overview",
                7.5,
                "/poster.jpg",
                "2022-01-01",
                "Test Movie",
                false,
                7.2,
                100
        );

        // Générez la chaîne attendue en fonction de la sortie réelle de la méthode toString()
        String expectedToString = String.format(
                "Titre: %s (%s)\n" +
                        "Langue originale: %s | Adulte: %s | Vidéo: %s\n" +
                        "Note moyenne: %.1f (sur %d votes)\n" +
                        "Genres: %s\n" +
                        "Popularité: %.2f\n" +
                        "Résumé: %s\n",
                movie.getTitle(), movie.getRelease_date(),
                movie.getOriginal_language().toUpperCase(), movie.isAdult() ? "Oui" : "Non",
                movie.isVideo() ? "Oui" : "Non",
                movie.getVote_average(), movie.getVote_count(),
                movie.getGenre_ids(),
                movie.getPopularity(),
                movie.getOverview().isEmpty() ? "Non disponible" : movie.getOverview()
        );

        assertEquals(expectedToString.trim(), movie.toString().trim());
    }
}
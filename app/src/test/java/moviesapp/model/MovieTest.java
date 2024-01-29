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
        // Création d'un film pour le test
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


        String expectedToString = "Movie{" +
                "adult=false, " +
                "backdropPath='/backdrop.jpg', " +
                "genreIds=[1, 2, 3], " +
                "id=123, " +
                "originalLanguage='en', " +
                "originalTitle='Original Title', " +
                "overview='Overview', " +
                "popularity=7.5, " +
                "posterPath='/poster.jpg', " +
                "releaseDate='2022-01-01', " +
                "title='Test Movie', " +
                "video=false, " +
                "voteAverage=7.2, " +
                "voteCount=100}";

        assertEquals(expectedToString, movie.toString());
    }
}

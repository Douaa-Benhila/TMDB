package moviesapp.model;

import moviesapp.JsonManager.MovieListResponse;
import moviesapp.model.Movie;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class MovieListResponseTest {

    @Test
    void testMovieListResponse() {
        // Création d'un objet MovieListResponse
        MovieListResponse movieListResponse = new MovieListResponse();

        // Initialisation des valeurs de test
        int page = 1;
        List<Movie> results = new ArrayList<>();
        results.add(new Movie(false,
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
                100));

        results.add(new Movie( false,
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
                100));
        int total_pages = 3;
        int total_results = 30;

        // Définir les valeurs pour l'objet MovieListResponse
        movieListResponse.setPage(page);
        movieListResponse.setResults(results);
        movieListResponse.setTotalPages(total_pages);
        movieListResponse.setTotalResults(total_results);

        // Vérifier les valeurs en utilisant les méthodes getters
        assertEquals(page, movieListResponse.getPage());
        assertEquals(results, movieListResponse.getResults());
        assertEquals(total_pages, movieListResponse.getTotalPages());
        assertEquals(total_results, movieListResponse.getTotalResults());
    }
}

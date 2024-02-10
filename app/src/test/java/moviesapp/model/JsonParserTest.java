package moviesapp.model;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import moviesapp.JsonManager.MovieListResponse;
import moviesapp.JsonManager.JsonParser;



public class JsonParserTest {

    // Test pour s'assurer que la conversion de la chaîne JSON en MovieListResponse fonctionne correctement
    @Test
    public void testParseMovieList() {
        // Création d'une chaîne JSON de test représentant une liste de films
        String json = "{\"page\": 1,\"results\": [{\"title\": \"Movie 1\",\"release_date\": \"2022-01-01\"},{\"title\": \"Movie 2\",\"release_date\": \"2022-02-01\"}],\"total_pages\": 1,\"total_results\": 2}";

        // Appel de la méthode de parsing
        MovieListResponse movieListResponse = JsonParser.parseMovieList(json);

        // Vérification que l'objet retourné n'est pas null
        assertNotNull(movieListResponse);

        // Vérification des valeurs des attributs de l'objet retourné
        assertEquals(1, movieListResponse.getPage());
        assertEquals(2, movieListResponse.getResults().size());
        assertEquals("Movie 1", movieListResponse.getResults().get(0).getTitle());
        assertEquals("2022-01-01", movieListResponse.getResults().get(0).getRelease_date());
        assertEquals("Movie 2", movieListResponse.getResults().get(1).getTitle());
        assertEquals("2022-02-01", movieListResponse.getResults().get(1).getRelease_date());
        assertEquals(1, movieListResponse.getTotalPages());
        assertEquals(2, movieListResponse.getTotalResults());
    }
}


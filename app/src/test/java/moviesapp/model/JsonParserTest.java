package moviesapp.model;

import moviesapp.JsonManager.JsonParser;
import moviesapp.JsonManager.MovieListResponse;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class JsonParserTest {

    @Test
    void testParseMovieList_ValidJson() {
        // JSON valide représentant une liste de films
        String json = "{\"page\":1,\"results\":[{\"adult\":false,\"backdrop_path\":\"/3KiwdVe0XqLNrBVYrpVMlvZaPdm.jpg\",\"genre_ids\":[99],\"id\":32571,\"original_language\":\"xx\",\"original_title\":\"Monkeyshines, No. 1\",\"overview\":\"Experimental film made to test the original cylinder format of the Kinetoscope and believed to be the first film shot in the United States. It shows a blurry figure in white standing in one place making large gestures and is only a few seconds long.\",\"popularity\":6.209,\"poster_path\":\"/a41Z3Owp1TsXcUlBCRhbb9eJjWW.jpg\",\"release_date\":\"1890-11-21\",\"title\":\"Monkeyshines, No. 1\",\"video\":false,\"vote_average\":4.9,\"vote_count\":90}],\"total_pages\":1,\"total_results\":1}";

        // Appel de la méthode parseMovieList pour convertir la chaîne JSON en un objet MovieListResponse
        MovieListResponse movieListResponse = JsonParser.parseMovieList(json);

        // Vérification si l'objet retourné n'est pas null
        assertNotNull(movieListResponse);

        // Vérification si la liste de résultats n'est pas vide
        assertFalse(movieListResponse.getResults().isEmpty());

        // Vérification si le titre du premier film est correct
        assertEquals("Monkeyshines, No. 1", movieListResponse.getResults().get(0).getTitle());
    }
}



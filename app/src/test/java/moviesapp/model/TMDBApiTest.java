package moviesapp.model;

import moviesapp.model.TMDBApi;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TMDBApiTest {
    @Test
    public void testSendGET() {
        try {
            String movieDetailsJson = TMDBApi.sendGET("/movie/872585", "");

            assertNotNull(movieDetailsJson, "La réponse JSON ne doit pas être nulle");

            // Examinez la structure réelle de la réponse JSON et ajustez cette vérification
            assertTrue(movieDetailsJson.contains("original_title"), "La réponse JSON doit contenir la clé 'original_title'");
        } catch (Exception e) {
            fail("Une exception ne devrait pas être lancée", e);
        }
    }

}

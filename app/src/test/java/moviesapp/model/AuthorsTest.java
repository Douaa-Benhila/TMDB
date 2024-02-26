package moviesapp.model;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import moviesapp.model.Authors;

public class AuthorsTest {
    private Authors authors = new Authors(Arrays.asList(
            new Author("Martin Dupont", "http://example.com/martin.jpg", "Biography of Martin Dupont"),
            new Author("Marie Martin", "http://example.com/marie.jpg", "Biography of Marie Martin"),
            new Author("François Cordonnier", "http://example.com/francois.jpg", "Biography of François Cordonnier")
    ));

    @Test
    public void testToString() {
        String expected = "Martin Dupont - http://example.com/martin.jpg - Biography of Martin Dupont, " +
                "Marie Martin - http://example.com/marie.jpg - Biography of Marie Martin, " +
                "François Cordonnier - http://example.com/francois.jpg - Biography of François Cordonnier";
        assertThat(authors.toString()).isEqualTo(expected);
    }
}
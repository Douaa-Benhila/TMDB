package moviesapp.model;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;

public class TmdbApiRequest {
    public static String fetchMoviesJson(String apiKey) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "https://api.themoviedb.org/3/discover/movie?language=en-US&page=1&primary_release_year=1890&apiKey=" + apiKey;

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .build();

        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String json = fetchMoviesJson("c2e5eea5f9078e7bd27be9838d32abf8");
        if (json != null) {
            System.out.println(json);
        }
    }
}

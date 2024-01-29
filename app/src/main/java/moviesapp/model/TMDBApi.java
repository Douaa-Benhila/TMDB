package moviesapp.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class TMDBApi {

    private static final String API_KEY = "c2e5eea5f9078e7bd27be9838d32abf8";
    private static final String BASE_URL = "https://api.themoviedb.org/3";

    public static String sendGET(String endpoint, String queryParams) throws IOException {
        String urlString = BASE_URL + endpoint + "?api_key=" + API_KEY + queryParams;

        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            return response.toString();
        } finally {
            connection.disconnect();
        }
    }

}

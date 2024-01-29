package moviesapp.model;

import com.google.gson.Gson;
import moviesapp.model.MovieListResponse;

public class JsonParser {

    public static MovieListResponse parseMovieList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MovieListResponse.class);
    }
}

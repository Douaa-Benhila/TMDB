package moviesapp.JsonManager;

import com.google.gson.Gson;

/*le but de cette classe la manipulation de données JSON.*/

public class JsonParser {

    /*
      Convertit une chaîne JSON en un objet de type MovieListResponse.
      @param json la chaîne JSON à convertir.
      @return un objet MovieListResponse représentant les données JSON converties.
     */

    public static MovieListResponse parseMovieList(String json) {
        Gson gson = new Gson();
        return gson.fromJson(json, MovieListResponse.class);
    }
}
package moviesapp.JsonManager;

import com.google.gson.Gson; // Gson is a Java library that can be used to convert Java Objects into their JSON representation.

/*le but de cette classe la manipulation de données JSON.*/

public class JsonParser {

    /*
      Convertit une chaîne JSON en un objet de type MovieListResponse.
      @param json la chaîne JSON à convertir.
      @return un objet MovieListResponse représentant les données JSON converties.
     */

    public static MovieListResponse parseMovieList(String json) { //responsable de la conversion d'une chaîne JSON en un objet de type MovieListResponse.
        Gson gson = new Gson();
        return gson.fromJson(json, MovieListResponse.class);
    }
}
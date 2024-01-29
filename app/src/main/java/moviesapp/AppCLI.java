package moviesapp;

import java.io.IOException;
import java.util.Scanner;

import static moviesapp.model.TMDBApi.sendGET;

public class AppCLI {
    public static void main(String[] args) {
        try {
            String popularMoviesJson = sendGET("/movie/2020", "");

            System.out.println("Réponse JSON pour les vidéos du film (ID : 872585) :");
            System.out.println(popularMoviesJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package moviesapp.model;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/*
 * Représente un film avec divers attributs tels que le titre, le genre, la date de sortie, et plus encore.
 * Cette classe fournit des getters et des setters pour accéder et modifier les propriétés du film.
 */

public class Movie {

    private boolean favorite; //Ajoutez un nouvel attribut favorite de type boolean pour indiquer si un film est ajouté aux favoris ou non.
    private boolean adult;
    private String backdrop_path;
    private List<Integer> genre_ids;
    private int id;
    private String original_language;
    private String original_title;
    private String overview;
    private double popularity;
    private String poster_path;
    private String release_date;
    private String title;
    private boolean video;
    private double vote_average;
    private int vote_count;

    /**
     *Construit une nouvelle instance de Movie.
     * @param adult Indique si le film est destiné aux adultes.
     * @param backdrop_path Le chemin vers l'image de fond du film.
     * @param genre_ids Une liste d'identifiants de genre associés au film.
     * @param id L'identifiant unique du film.
     * @param original_language La langue originale du film.
     * @param original_title Le titre original du film.
     * @param overview  Un bref résumé ou description du film.
     * @param popularity Le score de popularité du film.
     * @param poster_path Le chemin vers l'affiche du film.
     * @param release_date La date de sortie du film.
     * @param title Le titre du film.
     * @param video Indique si le film est une vidéo.
     * @param vote_average  La note moyenne du film.
     * @param vote_count  Le nombre de votes reçus par le film.
     */
    public Movie(boolean adult, String backdrop_path, List<Integer> genre_ids, int id, String original_language, String original_title, String overview, double popularity, String poster_path, String release_date, String title, boolean video, double vote_average, int vote_count) {
        this.adult = adult;
        this.backdrop_path = backdrop_path;
        this.genre_ids = genre_ids;
        this.id = id;
        this.original_language = original_language;
        this.original_title = original_title;
        this.overview = overview;
        this.popularity = popularity;
        this.poster_path = poster_path;
        this.release_date = release_date;
        this.title = title;
        this.video = video;
        this.vote_average = vote_average;
        this.vote_count = vote_count;
    }

    public boolean isAdult() {
        return adult;
    }

    public String getBackdrop_path() {
        return backdrop_path;
    }

    public List<Integer> getGenre_ids() {
        return genre_ids;
    }

    public int getId() {
        return id;
    }

    public String getOriginal_language() {
        return original_language;
    }

    public String getOriginal_title() {
        return original_title;
    }

    public String getOverview() {
        return overview;
    }

    public double getPopularity() {
        return popularity;
    }

    public String getPoster_path() {
        return poster_path;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTitle() {
        return title;
    }

    public boolean isVideo() {
        return video;
    }

    public double getVote_average() {
        return vote_average;
    }

    public int getVote_count() {
        return vote_count;
    }


    public void setAdult(boolean adult) {
        this.adult = adult;
    }

    public void setBackdrop_path(String backdrop_path) {
        this.backdrop_path = backdrop_path;
    }

    public void setGenre_ids(List<Integer> genre_ids) {
        this.genre_ids = genre_ids;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setOriginal_language(String original_language) {
        this.original_language = original_language;
    }

    public void setOriginal_title(String original_title) {
        this.original_title = original_title;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setPoster_path(String poster_path) {
        this.poster_path = poster_path;
    }

    public void setRelease_date(String release_date) {
        this.release_date = release_date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setVideo(boolean video) {
        this.video = video;
    }

    public void setVote_average(double vote_average) {
        this.vote_average = vote_average;
    }

    public void setVote_count(int vote_count) {
        this.vote_count = vote_count;
    }


    // Getters et setters pour l'attribut favori
    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        // Création d'une représentation de chaîne de caractères plus lisible pour un film
        String info = String.format(
                "Titre: %s (%s)\n",
                title, release_date
        );

        info += String.format(
                "Langue originale: %s | Adulte: %s | Vidéo: %s\n",
                original_language.toUpperCase(),
                adult ? "Oui" : "Non",
                video ? "Oui" : "Non"
        );

        info += String.format(
                "Note moyenne: %.1f (sur %d votes)\n",
                vote_average, vote_count
        );

        info += "Genres: " + genre_ids + "\n";

        info += "Popularité: " + String.format("%.2f\n", popularity);

        info += "Résumé: " + (overview.isEmpty() ? "Non disponible" : overview) + "\n";

        return info;
    }

    @Override
    public boolean equals(Object o) { // utilité de cette méthode est de voir si un film existait déjà
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Movie movie = (Movie) o;
        return id == movie.id;
    }

    @Override
    public int hashCode() { // permet d'attribuer un identifiant unique à chaque objet, ce qui facilite leur recherche et leur utilisation
        return Objects.hash(id);
    }

}



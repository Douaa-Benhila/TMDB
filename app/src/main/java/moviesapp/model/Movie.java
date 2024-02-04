package moviesapp.model;

import java.util.List;


/**
 * Represents a movie with various attributes such as title, genre, release date, and more.
 * This class provides getters and setters for accessing and modifying movie properties.
 */
public class Movie {
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
     * Constructs a new Movie instance.
     *
     * @param adult Indicates if the movie is for adults.
     * @param backdrop_path The path to the movie's backdrop image.
     * @param genre_ids A list of genre IDs associated with the movie.
     * @param id The unique identifier of the movie.
     * @param original_language The original language of the movie.
     * @param original_title The original title of the movie.
     * @param overview A brief overview or description of the movie.
     * @param popularity The popularity score of the movie.
     * @param poster_path The path to the movie's poster image.
     * @param release_date The release date of the movie.
     * @param title The title of the movie.
     * @param video Indicates if the movie is a video.
     * @param vote_average The average vote score of the movie.
     * @param vote_count The count of votes received by the movie.
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

    @Override
    public String toString() {
        return "Movie{" +
                "adult=" + adult +
                ", backdropPath='" + backdrop_path + '\'' +
                ", genreIds=" + genre_ids +
                ", id=" + id +
                ", originalLanguage='" + original_language + '\'' +
                ", originalTitle='" + original_title + '\'' +
                ", overview='" + overview + '\'' +
                ", popularity=" + popularity +
                ", posterPath='" + poster_path + '\'' +
                ", releaseDate='" + release_date + '\'' +
                ", title='" + title + '\'' +
                ", video=" + video +
                ", voteAverage=" + vote_average +
                ", voteCount=" + vote_count +
                '}';
    }

}



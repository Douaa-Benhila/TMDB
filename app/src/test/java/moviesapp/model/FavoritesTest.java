package moviesapp.model;

import java.util.Arrays;

public class FavoritesTest {
    public static void main(String[] args) {
        Movie movie1 = new Movie(
                false,
                "backdropPath1",
                Arrays.asList(1, 2, 3),
                1,
                "originalLanguage1",
                "originalTitle1",
                "overview1",
                10.5,
                "posterPath1",
                "2022-01-01",
                "Title 1",
                false,
                8.0,
                100
        );

        Movie movie2 = new Movie(
                true,
                "backdropPath2",
                Arrays.asList(4, 5, 6),
                2,
                "originalLanguage2",
                "originalTitle2",
                "overview2",
                15.2,
                "posterPath2",
                "2022-02-02",
                "Title 2",
                true,
                9.5,
                150
        );

        Movie movie3 = new Movie(
                false,
                "backdropPath3",
                Arrays.asList(7, 8, 9),
                3,
                "originalLanguage3",
                "originalTitle3",
                "overview3",
                20.0,
                "posterPath3",
                "2022-03-03",
                "Title 3",
                false,
                7.5,
                120
        );

        Favorites favorites = new Favorites();

        favorites.addFavoriteMovie(movie1);
        favorites.addFavoriteMovie(movie2);
        favorites.addFavoriteMovie(movie3);

        favorites.showFavoritesMovies();

        favorites.removeFavoriteMovie(movie2);

        favorites.showFavoritesMovies();
    }
}

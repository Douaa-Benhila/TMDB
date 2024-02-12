package moviesapp.JsonManager;

import moviesapp.model.Movie;

import java.util.List;

public class MovieListResponse {
    private int page;
    private List<Movie> results; //Liste des films retournés dans la réponse
    private int total_pages;
    private int total_results;

    public int getPage() { //@return Le numéro de la page des résultats
        return page;
    }

    public void setPage(int page) { // @param page Le numéro de la page des résultats à définir
        this.page = page;
    }

    public List<Movie> getResults() { // @return La liste des films retournés
        return results;
    }

    public void setResults(List<Movie> results) { // @param results La liste des films à définir
        this.results = results;
    }

    public int getTotalPages() { // @return Le nombre total de pages de résultats disponibles
        return total_pages;
    }

    public void setTotalPages(int total_pages) { // @param total_pages Le nombre total de pages de résultats à définir
        this.total_pages = total_pages;
    }

    public int getTotalResults() { // @return Le nombre total de résultats disponibles
        return total_results;
    }

    public void setTotalResults(int total_results) { //@param total_results Le nombre total de résultats à définir
        this.total_results = total_results;
    }
}

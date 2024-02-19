package moviesapp.JsonManager;

import moviesapp.model.Movie;

import java.util.List;

public class MovieListResponse {
    private int page;
    private List<Movie> results; //Liste des films retournés dans la réponse
    private int total_pages;
    private int total_results;

    /**
     * Obtient le numéro de la page des résultats.
     * @return Le numéro de la page des résultats.
     */

    public int getPage() { //@return Le numéro de la page des résultats
        return page;
    }
    /**
     * Définit le numéro de la page des résultats.
     * @param page Le numéro de la page des résultats à définir.
     */

    public void setPage(int page) { // @param page Le numéro de la page des résultats à définir
        this.page = page;
    }

    /**
     * Obtient la liste des films retournés dans la réponse.
     * @return La liste des films retournés.
     */

    public List<Movie> getResults() { // @return La liste des films retournés
        return results;
    }
    /**
     * Définit la liste des films retournés dans la réponse.
     * @param results La liste des films à définir.
     */

    public void setResults(List<Movie> results) { // @param results La liste des films à définir
        this.results = results;
    }

    /**
     * Obtient le nombre total de pages de résultats disponibles.
     * @return Le nombre total de pages de résultats disponibles.
     */
    public int getTotalPages() { // @return Le nombre total de pages de résultats disponibles
        return total_pages;
    }
    /**
     * Définit le nombre total de pages de résultats disponibles.
     * @param total_pages Le nombre total de pages de résultats à définir.
     */

    public void setTotalPages(int total_pages) { // @param total_pages Le nombre total de pages de résultats à définir
        this.total_pages = total_pages;
    }
    /**
     * Obtient le nombre total de résultats disponibles.
     * @return Le nombre total de résultats disponibles.
     */

    public int getTotalResults() { // @return Le nombre total de résultats disponibles
        return total_results;
    }

    /**
     * Définit le nombre total de résultats disponibles.
     * @param total_results Le nombre total de résultats à définir.
     */

    public void setTotalResults(int total_results) { //@param total_results Le nombre total de résultats à définir
        this.total_results = total_results;
    }
}

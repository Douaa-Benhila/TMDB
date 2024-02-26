package moviesapp.model;

public class Author {
    private String name;
    private String imageUrl;
    private String biography;

    private String directorId;

    public Author(String name, String imageUrl, String biography,String directorId) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.biography = biography;
        this.directorId = directorId;
    }

    public Author(String name, String imageUrl, String biography) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.biography = biography;
        this.directorId = ""; // Ou null, selon la logique de votre application
    }

    public String getName() {
        return name;
    }


    public String getImageUrl() {
        return imageUrl;
    }

    public String getBiography() {
        return biography;
    }

    public String getDirectorId() {
        return directorId;
    }

    // Setter pour directorId (si nécessaire)
    public void setDirectorId(String directorId) {
        this.directorId = directorId;
    }

    @Override
    public String toString() {
        return name + " - " + imageUrl + " - " + biography;
    }
}

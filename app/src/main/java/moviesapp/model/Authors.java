package moviesapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Authors {
    private List<String> authorsList;

    public Authors(List<String> authorsList) {
        this.authorsList = new ArrayList<>(authorsList);
    }


    public void addAuthor(String authorName) {
        authorsList.add(authorName);
    }


    public String getAuthorByName(String name) {
        return authorsList.stream()
                .filter(authorName -> authorName.equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }


    @Override
    public String toString() {
        return authorsList.stream()
                .collect(Collectors.joining(", "));
    }
}




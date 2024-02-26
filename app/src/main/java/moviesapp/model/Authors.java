package moviesapp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Authors {
    private List<Author> authorsList;

    public Authors(List<Author> authorsList) {
        this.authorsList = new ArrayList<>(authorsList);
    }

    @Override
    public String toString() {
        return authorsList.stream()
                .map(Author::toString)
                .collect(Collectors.joining(", "));
    }
}

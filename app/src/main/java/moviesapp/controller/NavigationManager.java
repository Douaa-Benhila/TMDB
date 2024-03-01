package moviesapp.controller;

import java.util.Stack;

public class NavigationManager {
    private static final Stack<String> history = new Stack<>();

    // Ajoute une page à l'historique
    public static void addPage(String pageName) {
        history.push(pageName);
    }

    // Récupère la dernière page visitée depuis l'historique
    public static String getPreviousPage() {
        if (!history.isEmpty()) {
            // Retire la page actuelle de l'historique
            history.pop();
        }
        // Renvoie la dernière page visitée s'il y en a une
        return history.isEmpty() ? null : history.peek();
    }
}

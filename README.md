#  TMDB  — Application Java de Recherche de Films

Bienvenue dans **TMDB**, une application conçue en Java permettant de rechercher et explorer des films à partir de l’API publique **The Movie Database (TMDB)**.  
L'objectif est de fournir un logiciel interactif  qui permet à l'utilisateur de **trouver des films**, **consulter leurs détails**, et **gérer une liste de favoris**.

---

##  Objectif du Projet

Cette application a été réalisée dans le cadre d’un projet universitaire. Elle vise à :

- **Rechercher** un film à partir de son **titre**, **année de sortie**, **note** ou **genre**
- **Afficher** les résultats sous forme de **posters interactifs**
- **Afficher les détails** complets d’un film sélectionné : titre original, réalisateur, acteurs, etc.
- **Gérer des favoris** : ajout, suppression et consultation
- **Explorer les films similaires**, les autres films d’un même réalisateur, et voir la **bande-annonce**

---

##  Comment est structuré le code ?

Voici les principales composantes du code :

###  `model/` — Gestion des données

- `Movie.java` : Représente un film (titre, date, note, genre, etc.)
- `Favorites.java` : Permet d’ajouter/supprimer un film aux favoris
- `MovieListResponse.java` : Traite les réponses JSON contenant plusieurs films
- `TMDBApi.java` : Fait les requêtes à l'API TMDB (récupération de films, détails, acteurs, etc.)

###  `cli/` — Interface en ligne de commande (mode texte)

- `AppCLI.java` : Lanceur de l’application en mode terminal
- `MovieSearchManager.java` : Permet la recherche de films via des filtres (titre, note, genre...)

###  `ui/` — Interface graphique (JavaFX)

- `App.java` : Point d’entrée de l’application graphique
- `AppController.java` : Gère la recherche depuis l’interface graphique
- `MovieTileController.java` : Affiche un film sous forme de vignette
- `MovieDetailsController.java` : Affiche les informations détaillées d’un film sélectionné
- `moviesapp.fxml`, `movieTile.fxml`, `MovieDetails.fxml` : Définissent l’interface visuelle

---

##  Comment ça fonctionne ? (Vue utilisateur)

1. **Lancement** : l'utilisateur ouvre l'application (interface graphique ou terminal)
2. **Recherche** : il tape un mot-clé ou choisit des filtres (année, genre, note)
3. **Résultat** :
   - en ligne de commande : une liste textuelle des films
   - en interface graphique : des affiches de films (posters) s'affichent
4. **Détails** :
   - en cliquant sur une affiche, on obtient :
     - titre original
     - synopsis
     - note
     - acteurs principaux
     - films similaires
     - films du même réalisateur
     - bande-annonce (si disponible)
5. **Favoris** : il peut ajouter/supprimer un film à sa liste de favoris

---

##  Points forts pédagogiques

- **Utilisation concrète d’une API REST**
- **Manipulation de données JSON**
- **Programmation orientée objet avancée**
- **Intégration d’une interface utilisateur moderne (JavaFX)**
- **Découverte de la gestion de projet avec Git (branches, merges, commits)**

---

## 🛠️ Technologies utilisées

- Langage : **Java 17**
- Interface graphique : **JavaFX**
- API : [The Movie Database (TMDB)](https://www.themoviedb.org/)
- Format de données : **JSON (via GSON)**
- Build tool : **Gradle**
- Hébergement du code : **Github**

---

## 📦 Installation & Lancement

### Prérequis
- Java 17+
- Gradle

### Lancement (via ligne de commande)
```bash
# Compiler le projet
gradle build

# Lancer l'application en CLI
java -cp build/classes/java/main cli.AppCLI
```

### Lancement (interface graphique JavaFX)
```bash
gradle run
```

---

## 📚 Ressources & Aide

- Tutoriels YouTube (API TMDB, JavaFX)
- Documentation Gson & TMDB
- Supports pédagogiques 

---


## 📌 Conclusion

**TMDB** est un projet complet permettant d’appliquer les connaissances en Java tout en créant une application ludique et utile. Il démontre comment construire un logiciel à la fois fonctionnel, évolutif et agréable à utiliser, même pour un public non technique.

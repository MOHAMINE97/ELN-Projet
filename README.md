# ELN Backend — Démarrage rapide

## Prérequis
- Java 21
- Maven (le wrapper `mvnw`/`mvnw.cmd` fourni dans le projet fonctionne sans installation Maven séparée)
- Docker (pour PostgreSQL)
## Lancer la base de données

```bash
docker compose up -d
```

Identifiants par défaut (voir `docker-compose.yml` et `application.properties`) :
- Base : `eln_db`
- Utilisateur : `eln_user`
- Mot de passe : `eln_password`
- Port hôte : `5434`

Vérifiez que `spring.datasource.url` dans `application.properties` pointe bien vers
`jdbc:postgresql://127.0.0.1:5434/eln_db`.

## Lancer l'application

```bash
./mvnw spring-boot:run
```

Sous Windows : `mvnw.cmd spring-boot:run`

L'API démarre sur `http://localhost:8081` (modifiable via `server.port` dans
`application.properties`).

## Variable d'environnement obligatoire (sécurité)
### La clé secrète JWT ne doit plus être en dur dans le code.

### Définissez la variable d'environnement `JWT_SECRET` (minimum 32 caractères) :

* IntelliJ : Run → Edit Configurations → Environment variables →
  `JWT_SECRET=votre-cle-secrete-longue-et-aleatoire`
* Ligne de commande :
```bash
  export JWT_SECRET=votre-cle-secrete-longue-et-aleatoire
  ./mvnw spring-boot:run
```

## Tester rapidement (avec curl)

### 1. Créer un compte

```bash
curl -X POST http://localhost:8081/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"moi@labo.fr","motDePasse":"motdepasse123","nom":"Mohamed"}'
```

La réponse contient un `token` JWT.
Le rôle est forcé à `TECHNICIEN` à l'inscription. Seul un `ADMIN` peut ensuite changer le rôle d'un utilisateur.
### 2. Se connecter (si vous avez déjà un compte)

```bash
curl -X POST http://localhost:8081/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"moi@labo.fr","motDePasse":"motdepasse123"}'
```

### 3. Créer un échantillon (avec le token)

```bash
TOKEN="collez_le_token_ici"
 
curl -X POST http://localhost:8081/api/echantillons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"reference":"SIC-2026-001","materiau":"SiC","description":"Test decoupe laser"}'
```

### 4. Ajouter une étape de procédé

```bash
curl -X POST http://localhost:8081/api/echantillons/1/etapes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"type":"GRAVURE_PLASMA","dateExecution":"2026-09-19T10:00:00","pressionMbar":0.05,"puissancePlasmaWatts":150,"dureeMinutes":12}'
```

### 5. Ajouter un résultat de caractérisation

```bash
curl -X POST http://localhost:8081/api/echantillons/1/resultats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"type":"EPAISSEUR","dateMesure":"2026-09-19T14:00:00","valeur":250.5,"unite":"nm"}'
```

### 6. Consulter mes échantillons

```bash
curl http://localhost:8081/api/echantillons/mine -H "Authorization: Bearer $TOKEN"
```

### 7. Consulter le détail complet d'un échantillon

```bash
curl http://localhost:8081/api/echantillons/1 -H "Authorization: Bearer $TOKEN"
```

## Rôles et droits
|   Rôle           | Droits                                               |
|------------------|------------------------------------------------------|
|TECHNICIEN        | Crée et consulte uniquement ses propres échantillons |
|CHERCHEUR / ADMIN | Voit tous les échantillons + peut supprimer          |

Le contrôle d'accès est appliqué à la fois au niveau HTTP (`SecurityConfig`) et métier (`EchantillonService`).

## Dépannage rapide

- `Failed to configure a DataSource`  : PostgreSQL n'est pas
  lancé ou le port est incorrect. Vérifiez `docker ps` et `spring.datasource.url`.
- **`Port 8081 was already in use`** : une instance précédente de l'application tourne encore.
- **Erreur d'authentification JWT** : vérifiez que la variable d'environnement `JWT_SECRET` est bien définie.
- **Erreurs Lombok à la compilation** : vérifiez que `pom.xml` déclare bien Lombok en
  `annotationProcessorPaths` dans la configuration de `maven-compiler-plugin`.
## Ce qui est fait

- Modèle de données (4 entités JPA) — voir `MODELE_DONNEES.md`
- Repositories Spring Data JPA
- DTOs (jamais d'exposition directe des entités JPA dans l'API)
- Service métier avec gestion d'erreurs (`ResourceNotFoundException`, validation)
- Contrôleur REST `EchantillonController` (CRUD + sous-ressources étapes/résultats)
- Gestion d'erreurs centralisée (`GlobalExceptionHandler`) avec réponses JSON structurées
- **Authentification JWT complète** : inscription, connexion, filtre de vérification de token,
  protection des endpoints)
- Clé JWT externalisée via variable d'environnement

## Tests Postman

1. Importer `postman/eln_postman_collection.json` dans Postman.
2. Vérifier que `baseUrl` vaut `http://localhost:8081`.
3. Lancer la collection (36 requêtes).

## Prochaine étape

- **Tests JUnit + Testcontainers**
- **Le front-end (Angular ou React)** qui consomme cette API : formulaire de connexion stockant le
token, liste des échantillons, formulaire de création avec ajout dynamique d'étapes.
 






# ELN Backend — Démarrage rapide

## Prérequis
- Java 21
- Maven (le wrapper `mvnw`/`mvnw.cmd` fourni dans le projet fonctionne sans installation Maven séparée)
- Docker (pour PostgreSQL, ou une instance PostgreSQL locale)
## Lancer la base de données

```bash
docker compose up -d
```

Identifiants par défaut (voir `docker-compose.yml` et `application.properties`) :
- Base : `eln_db`
- Utilisateur : `eln_user`
- Mot de passe : `eln_password`
## Lancer l'application

```bash
./mvnw spring-boot:run
```

Sous Windows : `mvnw.cmd spring-boot:run`

L'API démarre sur `http://localhost:8080` (modifiable via `server.port` dans
`application.properties`).

## Tester rapidement (avec curl)

### 1. Créer un compte

```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{"email":"moi@labo.fr","motDePasse":"motdepasse123","nom":"Mohamed"}'
```

La réponse contient un `token` JWT — copiez-le pour la suite. Par défaut, le compte créé a le
rôle `TECHNICIEN`. Pour créer un compte `CHERCHEUR` ou `ADMIN`, ajoutez `"role":"CHERCHEUR"`
(ou `"ADMIN"`) dans le corps de la requête.

### 2. Se connecter (si vous avez déjà un compte)

```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"moi@labo.fr","motDePasse":"motdepasse123"}'
```

### 3. Créer un échantillon (avec le token)

```bash
TOKEN="collez_le_token_ici"
 
curl -X POST http://localhost:8080/api/echantillons \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"reference":"SIC-2026-001","materiau":"SiC","description":"Test decoupe laser"}'
```

### 4. Ajouter une étape de procédé

```bash
curl -X POST http://localhost:8080/api/echantillons/1/etapes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"type":"GRAVURE_PLASMA","dateExecution":"2026-09-19T10:00:00","pressionMbar":0.05,"puissancePlasmaWatts":150,"dureeMinutes":12}'
```

### 5. Ajouter un résultat de caractérisation

```bash
curl -X POST http://localhost:8080/api/echantillons/1/resultats \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer $TOKEN" \
  -d '{"type":"EPAISSEUR","dateMesure":"2026-09-19T14:00:00","valeur":250.5,"unite":"nm"}'
```

### 6. Consulter mes échantillons

```bash
curl http://localhost:8080/api/echantillons/mine -H "Authorization: Bearer $TOKEN"
```

### 7. Consulter le détail complet d'un échantillon

```bash
curl http://localhost:8080/api/echantillons/1 -H "Authorization: Bearer $TOKEN"
```

## Rôles et droits

- **TECHNICIEN** (par défaut à l'inscription) : crée et consulte ses propres échantillons.
- **CHERCHEUR** / **ADMIN** : en plus, peuvent lister tous les échantillons
  (`GET /api/echantillons`) et en supprimer (`DELETE /api/echantillons/{id}`).
## Dépannage rapide

- **`Failed to configure a DataSource` / erreur de Dialect Hibernate** : PostgreSQL n'est pas
  lancé ou n'est pas joignable. Vérifiez `docker ps` (le conteneur doit apparaître "Up"), et
  que `spring.datasource.url` dans `application.properties` correspond bien au
  `docker-compose.yml`.
- **`Port 8080 was already in use`** : une instance précédente de l'application tourne encore.
  Arrêtez-la, ou changez `server.port` dans `application.properties`.
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
  protection des endpoints par rôle (`@PreAuthorize`, règles HTTP dans `SecurityConfig`)

## Tests Postman

1. Importer `postman/eln_postman_collection.json` dans Postman.
2. Vérifier que `baseUrl` vaut `http://localhost:8081`.
3. Créer l'admin de test une seule fois :
   lancer la requête 0a, puis exécuter en SQL :
   `UPDATE utilisateurs SET role = 'ADMIN' WHERE email = 'admin@labo.fr';`
4. Lancer la collection avec le Collection Runner (28 tests).

## Prochaine étape

Le front-end (Angular ou React) qui consomme cette API : formulaire de connexion stockant le
token, liste des échantillons, formulaire de création avec ajout dynamique d'étapes.
 






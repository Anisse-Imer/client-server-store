# Configuration Base de Données MySQL

## Prérequis
- Docker installé sur votre machine
- Java avec JDBC MySQL driver

## Documentation
Lien vers la documentation complète : https://anisse-imerzoukene.notion.site/Architecture-Client-Serveur-1c3d37ed979d80838844feb7a8d7177b

## Démarrage du conteneur MySQL

```bash
docker run --name mysql-container \
  -e MYSQL_ROOT_PASSWORD=root \
  -e MYSQL_ALLOW_EMPTY_PASSWORD=yes \
  -p 3306:3306 \
  -d mysql:latest
```

## Création des bases de données

Connectez-vous au conteneur MySQL :

```bash
docker exec -it mysql-container mysql -u root
```

Exécutez les commandes SQL suivantes :

```sql
-- Création de la base de données store_bdd
CREATE DATABASE IF NOT EXISTS store_bdd;

-- Création de la base de données main_bdd
CREATE DATABASE IF NOT EXISTS main_bdd;

-- Afficher les bases de données créées
SHOW DATABASES;

-- Sortir de MySQL
EXIT;
```

## Configuration JDBC

La configuration actuelle utilise :
- URL : `jdbc:mysql://localhost:3306/store_bdd`
- Utilisateur : `root`
- Mot de passe : (vide)
- Fichier de schéma : `resources/init.sql`

## Arrêt du conteneur

```bash
docker stop mysql-container
docker rm mysql-container
```

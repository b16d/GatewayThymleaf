# Thymeleaf Portal

Un portail Spring Boot 3.x pour enregistrer et gérer des applications Thymeleaf. Permet de mettre en œuvre deux modes d'enregistrement : upload ZIP ou enregistrement par URL distante.

## 🚀 Caractéristiques

- **Enregistrement d'applications uploadées** : Téléchargez un ZIP contenant vos templates et ressources Thymeleaf
- **Enregistrement d'applications distantes** : Enregistrez une URL pour accéder à une application Thymeleaf hébergée
- **Authentification Spring Security** : Rôles ADMIN (enregistrement) et USER (accès)
- **Stockage sécurisé** : Validation ZIP, prévention de traversée de répertoires, limites de taille (10MB)
- **API REST** : Endpoints pour gérer les applications par programmation
- **Interface Thymeleaf** : Portail web élégant et réactif
- **Docker Support** : Dockerfile et docker-compose fournis
- **Tests unitaires et d'intégration** : Suite de tests avec Mockito et Spring Test
- **CI/CD GitHub Actions** : Build, test, et push Docker automatisés

## 📋 Prérequis

- Java 17+
- Maven 3.8+
- Docker (optionnel)
- Docker Compose (optionnel)

## 🛠️ Installation et Démarrage

### Mode Développement (H2 en mémoire)

1. **Clone/Ouvre le projet** dans IntelliJ IDEA

2. **Build avec Maven** :
   ```bash
   mvn clean install
   ```

3. **Exécute l'application** :
   ```bash
   mvn spring-boot:run
   ```

4. **Accède au portail** : http://localhost:8080/

5. **Identifiants de démo** :
   - **Admin** : username: `admin` / password: `admin123`
   - **User** : username: `user` / password: `user123`

6. **Accède à H2 Console** : http://localhost:8080/h2-console
   - JDBC URL: `jdbc:h2:mem:testdb`
   - User: `sa`
   - Password: (vide)

### Mode Production avec Docker

1. **Build et lance avec Docker Compose** :
   ```bash
   docker-compose up -d
   ```

   Cela démarre :
   - PostgreSQL 16 sur le port 5432
   - Application Spring Boot sur le port 8080

2. **Accède à l'application** : http://localhost:8080/

3. **Arrête les services** :
   ```bash
   docker-compose down
   ```

## 📦 Structure du Projet

```
src/main/java/com/article/ai/gatewayfront/
├── entity/
│   └── RegisteredApp.java        # Entité JPA pour les apps
├── repository/
│   └── RegisteredAppRepository.java
├── service/
│   ├── AppService.java           # Logique métier
│   └── StorageService.java       # Gestion des fichiers ZIP
├── controller/
│   ├── AppController.java        # Contrôleur Web + API REST
│   └── ProxyController.java      # Proxy pour apps distantes
├── config/
│   ├── SecurityConfig.java       # Configuration Spring Security
│   └── WebConfig.java            # Configuration web
└── dto/
    ├── AppRegistrationRequest.java
    └── AppResponse.java

src/main/resources/
├── templates/
│   ├── layout.html               # Layout principal
│   ├── app-list.html             # Liste des apps
│   ├── app-register.html         # Formulaire d'enregistrement
│   ├── app-detail.html           # Détails d'une app
│   └── login.html                # Page de connexion
├── application.properties        # Config dev (H2)
└── application-prod.properties   # Config prod (PostgreSQL)

src/test/java/
├── service/
│   ├── AppServiceTest.java
│   └── StorageServiceTest.java
└── controller/
    └── AppControllerIntegrationTest.java
```

## 🔌 API REST

### Enregistrer une application uploadée

```bash
curl -X POST http://localhost:8080/api/apps/register/upload \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -F "appName=MyApp" \
  -F "description=My Thymeleaf App" \
  -F "zipFile=@path/to/app.zip"
```

### Enregistrer une application distante

```bash
curl -X POST http://localhost:8080/api/apps/register/remote \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -d '{
    "appName": "RemoteApp",
    "description": "Application hébergée",
    "remoteBaseUrl": "https://example.com/myapp"
  }'
```

### Récupérer toutes les apps

```bash
curl -X GET http://localhost:8080/api/apps \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### Récupérer une app par ID

```bash
curl -X GET http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### Supprimer une app

```bash
curl -X DELETE http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

## 🧪 Tests

Exécute tous les tests :
```bash
mvn test
```

Exécute avec couverture :
```bash
mvn test jacoco:report
```

Tests inclus :
- `StorageServiceTest` : Validation et extraction ZIP
- `AppServiceTest` : Logique métier (enregistrement, récupération, suppression)
- `AppControllerIntegrationTest` : Endpoints web et API, authentification

## 🔐 Sécurité

- **Authentification** : Spring Security avec utilisateurs en mémoire (dev) ou base de données (prod)
- **Autorisation** : Rôles ADMIN/USER sur les endpoints sensibles
- **Validation ZIP** : Prévention de traversée de répertoires (`..`), vérification des chemins
- **Limites de taille** : 10MB par fichier, 100KB par entrée ZIP
- **Sanitisation** : Headers et URLs validées

## 🐳 Déploiement Docker

### Build l'image

```bash
docker build -t gateway-front:latest .
```

### Exécute en standalone

```bash
docker run -p 8080:8080 \
  -e SPRING_PROFILES_ACTIVE=prod \
  -e SPRING_DATASOURCE_URL=jdbc:postgresql://host:5432/gatewayfront \
  gateway-front:latest
```

### Structure des uploads

Les fichiers uploadés sont stockés dans `/data/uploads/app-{id}/`

## 📝 Fichiers de Configuration

### `application.properties` (Dev)
```properties
spring.jpa.hibernate.ddl-auto=create-drop
spring.datasource.url=jdbc:h2:mem:testdb
app.upload.dir=uploads
```

### `application-prod.properties` (Production)
```properties
spring.jpa.hibernate.ddl-auto=validate
spring.datasource.url=jdbc:postgresql://postgres:5432/gatewayfront
app.upload.dir=/data/uploads
```

## 🚢 CI/CD avec GitHub Actions

Le workflow `.github/workflows/build-and-test.yml` :
- Build avec Maven
- Exécute les tests
- Build l'image Docker
- Push vers Docker Hub (sur branche main)

Secrets requis pour Docker Hub :
- `DOCKER_USERNAME`
- `DOCKER_PASSWORD`

## 📚 Dépendances Principales

- **Spring Boot 3.5.9**
- **Spring Data JPA**
- **Spring Security**
- **Thymeleaf**
- **Apache Commons Compress** (ZIP handling)
- **H2 Database** (dev)
- **PostgreSQL Driver** (prod)
- **JUnit 5 + Mockito** (testing)

## 🤝 Exemple Complet de Workflow

1. **Connexion en tant qu'admin** :
   ```
   Username: admin
   Password: admin123
   ```

2. **Enregistrement d'une app distante** :
   - Cliquez sur "Register App"
   - Remplissez le formulaire
   - Sélectionnez "Remote URL"
   - Entrez l'URL de base (ex: https://example.com/myapp)
   - Cliquez "Register Application"

3. **Accès à l'app** :
   - Page d'accueil affiche l'app enregistrée
   - Cliquez "View" pour voir les détails
   - Cliquez "Access Remote App" pour accéder à l'app externe

4. **Enregistrement d'une app uploadée** :
   - Créez un ZIP contenant `templates/` et `static/`
   - Enregistrez avec "Uploaded ZIP"
   - Téléchargez le ZIP
   - Accédez via `/uploaded/app-{id}/index.html`

## 🐛 Troubleshooting

**Port 8080 déjà utilisé** :
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

**Base de données PostgreSQL pas disponible** :
```bash
docker-compose up -d postgres
```

**Droits d'accès aux uploads** :
```bash
chmod -R 755 uploads/
```

## 📄 Licence

MIT License

## 👨‍💻 Auteur

Créé comme exemple de portail d'applications Thymeleaf.

---

**Pour démarrer rapidement** :
```bash
# Dev
mvn spring-boot:run

# Docker
docker-compose up
```

Accédez à http://localhost:8080/ avec les identifiants de démo !


# Configuration d'Environnement

Ce fichier documente les variables d'environnement utilisées par l'application.

## Variables d'Environnement

### Base de Données

**Dev (H2)** - Automatique, pas de config nécessaire

**Prod (PostgreSQL)** :
```bash
DB_USER=gatewayuser              # Username PostgreSQL
DB_PASSWORD=gatewaypassword      # Password PostgreSQL
SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/gatewayfront
```

### Stockage Fichiers

```bash
app.upload.dir=/data/uploads     # Répertoire pour apps uploadées
                                  # Dev : uploads/
                                  # Prod : /data/uploads
```

### Profils Spring

```bash
SPRING_PROFILES_ACTIVE=dev       # Utilise H2
# ou
SPRING_PROFILES_ACTIVE=prod      # Utilise PostgreSQL
```

### Logging

```bash
LOGGING_LEVEL_ROOT=INFO          # Niveau global
LOGGING_LEVEL_COM_ARTICLE_AI_GATEWAYFRONT=DEBUG  # App package
```

### Serveur

```bash
SERVER_PORT=8080                 # Port (défaut 8080)
SERVER_SERVLET_CONTEXT_PATH=/    # Context path (défaut /)
```

---

## Utilisation

### Avec Maven (Dev)

```bash
# Défaut : H2, port 8080, uploads/
mvn spring-boot:run

# Custom port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"

# Prod mode
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Avec Docker

Dans `docker-compose.yml`, les variables sont définies :
```yaml
environment:
  SPRING_PROFILES_ACTIVE: prod
  DB_USER: gatewayuser
  DB_PASSWORD: gatewaypassword
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/gatewayfront
```

### Avec Java JAR

```bash
# Standalone JAR
java -jar gateway-front-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/gatewayfront \
  --spring.datasource.username=gatewayuser \
  --spring.datasource.password=gatewaypassword
```

---

## Profils de Configuration

### Profil `dev` (Défaut)

**File** : `application.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.hibernate.ddl-auto=create-drop
app.upload.dir=uploads
logging.level.root=INFO
logging.level.com.article.ai=DEBUG
```

**Avantages** :
- Pas de DB externe requise
- Données réinitialisées à chaque démarrage (dev friendly)
- Logs détaillés

---

### Profil `prod`

**File** : `application-prod.properties`

```properties
spring.datasource.url=jdbc:postgresql://postgres:5432/gatewayfront
spring.datasource.username=${DB_USER:gatewayuser}
spring.datasource.password=${DB_PASSWORD:gatewaypassword}
spring.jpa.hibernate.ddl-auto=validate
app.upload.dir=/data/uploads
logging.level.root=WARN
```

**Avantages** :
- Données persistées
- Validation DB schema
- Logs minimalistes
- Configuration via env vars

---

## Exemples de Configuration

### Local Development (H2)

```bash
mvn spring-boot:run
# Accès : http://localhost:8080/
# H2 Console : http://localhost:8080/h2-console
```

### Local with PostgreSQL

```bash
# Start PostgreSQL
docker-compose -f docker-compose.postgres-only.yml up -d

# Run app
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"

# ou via env vars
export SPRING_PROFILES_ACTIVE=prod
export DB_USER=gatewayuser
export DB_PASSWORD=gatewaypassword
mvn spring-boot:run
```

### Docker Production

```bash
docker-compose up -d
# Automatique : app + PostgreSQL + network
```

### Kubernetes (exemple)

```yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: gatewayfront
spec:
  template:
    spec:
      containers:
      - name: app
        image: gateway-front:latest
        ports:
        - containerPort: 8080
        env:
        - name: SPRING_PROFILES_ACTIVE
          value: prod
        - name: DB_USER
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: username
        - name: DB_PASSWORD
          valueFrom:
            secretKeyRef:
              name: db-secret
              key: password
        - name: SPRING_DATASOURCE_URL
          value: jdbc:postgresql://postgres-service:5432/gatewayfront
```

---

## Valeurs par Défaut

| Variable | Default | Notes |
|----------|---------|-------|
| `SPRING_PROFILES_ACTIVE` | `dev` | Voir `application.properties` |
| `SERVER_PORT` | `8080` | Défini dans `application.properties` |
| `app.upload.dir` | `uploads` (dev) / `/data/uploads` (prod) | Créé automatiquement |
| `DB_USER` | `sa` (H2) / `gatewayuser` (PG) | Configuré en mémoire/env |
| `DB_PASSWORD` | `` (H2) / `gatewaypassword` (PG) | |

---

## Configuration Avancée

### Désactiver CSRF (pour API)

Dans `SecurityConfig.java` :
```java
http.csrf(csrf -> csrf.disable())  // Actuellement désactivé
```

En production, utiliser des tokens CSRF.

### Custom Upload Directory

```bash
java -jar app.jar --app.upload.dir=/custom/path
```

### Connection Pooling (HikariCP)

Ajouter à `application.properties` :
```properties
spring.datasource.hikari.maximum-pool-size=10
spring.datasource.hikari.minimum-idle=2
```

### Logs structurés (JSON)

Ajouter dépendance `spring-boot-starter-logging` avec JSON encoder (ex: Logback JSON).

---

## Troubleshooting

### "Database URL must not be null"
```
→ Profil `prod` activé sans vars d'env DB
→ Solution : Exporte DB_USER et DB_PASSWORD avant démarrage
```

### "Cannot create directory: /data/uploads"
```
→ Droits d'accès insuffisants
→ Solution : chmod 755 /data/ ou utiliser répertoire writable
```

### "Port 8080 already in use"
```
→ Autre process sur port 8080
→ Solution : --server.port=9090 ou arrête le process existant
```

### "Profil invalide"
```
→ SPRING_PROFILES_ACTIVE=invalid
→ Solution : Utilise `dev` ou `prod`
```

---

## Fichiers de Référence

- `pom.xml` - Propriétés Maven (versions, plugins)
- `application.properties` - Configuration dev
- `application-prod.properties` - Configuration prod
- `src/main/java/.../config/SecurityConfig.java` - Config security
- `src/main/java/.../config/WebConfig.java` - Config web/resources
- `docker-compose.yml` - Env vars Docker

---

## Quick Reference

```bash
# Dev H2
mvn spring-boot:run

# Dev PostgreSQL
docker-compose -f docker-compose.postgres-only.yml up -d
export SPRING_PROFILES_ACTIVE=prod
mvn spring-boot:run

# Docker full stack
docker-compose up -d

# Custom port
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"

# JAR file
java -jar target/gateway-front-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --spring.datasource.url=jdbc:postgresql://localhost:5432/gatewayfront
```

---

Pour plus de détails, consulte :
- `README.md` - Documentation générale
- `API_DOCUMENTATION.md` - Endpoints API
- `QUICK_START.md` - Démarrage rapide


# 🐛 Troubleshooting Guide

Guide pour résoudre les problèmes courants du Thymeleaf Portal.

---

## Build & Compilation

### ❌ "Maven command not found"

```
Error: mvn: command not found
```

**Solutions** :

1. **Installe Maven** :
   ```bash
   # macOS
   brew install maven
   
   # Windows (Chocolatey)
   choco install maven
   
   # Linux
   sudo apt-get install maven
   ```

2. **Vérifie l'installation** :
   ```bash
   mvn --version
   ```

3. **Ajoute à PATH** (Windows) :
   - `C:\Program Files\Apache Software Foundation\apache-maven-3.x.x\bin`

---

### ❌ "Java version not supported"

```
Error: Java version (11) does not match (17)
```

**Solutions** :

1. **Installe Java 17** :
   ```bash
   # macOS
   brew install openjdk@17
   
   # Windows
   choco install openjdk17
   ```

2. **Configure IntelliJ** :
   - File → Project Structure
   - Project SDK → Éclipse-Temurin-17
   
3. **Vérifie la version** :
   ```bash
   java -version
   # java version "17.x.x"
   ```

---

### ❌ "Compilation error: cannot find symbol"

```
Error: cannot find symbol
symbol: class RegisteredApp
```

**Solutions** :

1. **Recharge Maven** :
   ```bash
   mvn clean install -U
   ```

2. **Invalida Cache IntelliJ** :
   - File → Invalidate Caches
   - Sélecte "Invalidate and Restart"

3. **Vérifie les imports** :
   ```java
   import com.article.ai.gatewayfront.entity.RegisteredApp;
   ```

---

### ❌ "Missing dependency"

```
[ERROR] Failed to execute goal on project gateway-front:
[ERROR] Could not resolve dependencies for project
```

**Solutions** :

1. **Télécharge les dépendances** :
   ```bash
   mvn dependency:resolve
   ```

2. **Nettoie et rebuild** :
   ```bash
   mvn clean compile
   ```

3. **Vérifie .m2 repository** :
   - Windows: `%USERPROFILE%\.m2\repository`
   - Unix: `~/.m2/repository`
   - Supprime si corrompu et redéploie

---

## Runtime Issues

### ❌ "Port 8080 already in use"

```
Port 8080 is already in use
```

**Solutions** :

1. **Utilise un autre port** :
   ```bash
   mvn spring-boot:run \
     -Dspring-boot.run.arguments="--server.port=9090"
   ```

2. **Trouve et arrête le process** :
   ```bash
   # Windows PowerShell
   netstat -ano | findstr :8080
   taskkill /PID <PID> /F
   
   # macOS/Linux
   lsof -i :8080
   kill -9 <PID>
   ```

3. **Docker** :
   ```bash
   docker ps | grep 8080
   docker stop <container-id>
   ```

---

### ❌ "Application failed to start"

```
APPLICATION FAILED TO START
Description: Failed to configure a DataSource
```

**Causes courantes** :

#### Problème Database H2

```bash
# Solution
mvn clean spring-boot:run -Dspring.jpa.hibernate.ddl-auto=create-drop
```

#### Problème PostgreSQL (mode prod)

```bash
# Vérifie la connexion
docker-compose logs postgres

# Relance la base
docker-compose restart postgres

# ou avec postgres-only
docker-compose -f docker-compose.postgres-only.yml up -d
```

---

### ❌ "Cannot access H2 Console"

```
HTTP 404 on http://localhost:8080/h2-console
```

**Solutions** :

1. **H2 est désactivé en prod** :
   ```bash
   # Assure-toi de démarrer en dev
   mvn spring-boot:run  # Not in prod mode
   ```

2. **Activeassure le profil dev** :
   ```properties
   # application.properties
   spring.h2.console.enabled=true
   ```

3. **Redémarrage du serveur** :
   ```bash
   mvn clean spring-boot:run
   ```

---

## Authentication Issues

### ❌ "Unauthorized - 401"

```
HTTP 401 Unauthorized
```

**Solutions** :

1. **Vérifie les credentials** :
   - Admin: `admin` / `admin123`
   - User: `user` / `user123`

2. **Vérifie l'auth header** :
   ```bash
   # Base64 encode : admin:admin123 = YWRtaW46YWRtaW4xMjM=
   curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
     http://localhost:8080/api/apps
   ```

3. **Base64 encode online** :
   ```python
   import base64
   base64.b64encode(b"admin:admin123").decode()
   # YWRtaW46YWRtaW4xMjM=
   ```

---

### ❌ "Forbidden - 403"

```
HTTP 403 Forbidden
```

**Solutions** :

1. **Vérifie le rôle** :
   - `/register` requiert `ADMIN`
   - `/api/apps/register` requiert `ADMIN`
   - `/` requiert `USER` ou `ADMIN`

2. **Utilise un admin** :
   ```bash
   curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
     http://localhost:8080/api/apps/register/remote
   ```

3. **Vérifiez SecurityConfig** :
   ```java
   // Dans SecurityConfig.java
   .requestMatchers("/api/apps/register").hasRole("ADMIN")
   ```

---

## File Upload Issues

### ❌ "Maximum upload size exceeded"

```
The field zipFile exceeds its maximum permitted size of 10485760 bytes
```

**Solutions** :

1. **Réduis la taille du fichier** :
   - Limite actuelle : 10MB
   - Compresse ton ZIP mieux

2. **Augmente la limite** :
   ```properties
   # application.properties
   spring.servlet.multipart.max-file-size=20MB
   spring.servlet.multipart.max-request-size=20MB
   ```

3. **Divise ton ZIP** :
   - Crée plusieurs ZIPs
   - Enregistre-les séparément

---

### ❌ "Invalid path in ZIP: .."

```
SecurityException: Invalid path in ZIP: ../etc/passwd
```

**Cause** : Tentative de path traversal attaque

**Solution** : Utilise un ZIP valide sans chemins relatifs

```bash
# ✅ Bon
zip -r app.zip templates/ static/

# ❌ Mauvais
zip -r app.zip ../../../etc/
```

---

### ❌ "Failed to extract ZIP"

```
IOException: Failed to process ZIP file
```

**Solutions** :

1. **Vérifie le ZIP** :
   ```bash
   unzip -t app.zip
   ```

2. **Récréé le ZIP** :
   ```bash
   cd myapp
   zip -r ../myapp.zip .
   ```

3. **Vérifie les permissions** :
   ```bash
   chmod 755 uploads/
   ```

---

## Database Issues

### ❌ "Database is locked" (H2)

```
Database is locked
```

**Solutions** :

1. **Arrête l'application** et redémarrage
2. **Supprime les fichiers H2** :
   ```bash
   rm -f testdb.*
   ```
3. **Utilise H2 in-memory** (déjà configuré par défaut)

---

### ❌ "Cannot connect to PostgreSQL"

```
FATAL: Ident authentication failed for user "gatewayuser"
```

**Solutions** :

1. **Vérifie que PostgreSQL tourne** :
   ```bash
   docker-compose logs postgres
   ```

2. **Credentials corrects** :
   ```yaml
   # docker-compose.yml
   POSTGRES_USER: gatewayuser
   POSTGRES_PASSWORD: gatewaypassword
   ```

3. **Redémarrage le service** :
   ```bash
   docker-compose restart postgres
   ```

4. **Vérifie l'URL de connexion** :
   ```properties
   # application-prod.properties
   spring.datasource.url=jdbc:postgresql://postgres:5432/gatewayfront
   ```

---

### ❌ "Database migration failed"

```
ERROR: Unexpected error in migration [V1__Initial_schema.sql]
```

**Solutions** :

1. **Utilise validate au lieu de migrate** :
   ```properties
   # application-prod.properties
   spring.jpa.hibernate.ddl-auto=validate
   ```

2. **Réinitialise la base** :
   ```bash
   docker-compose down -v
   docker-compose up -d
   ```

---

## Docker Issues

### ❌ "Docker daemon not running"

```
Cannot connect to Docker daemon
```

**Solutions** :

1. **Démarre Docker** :
   ```bash
   # Windows (PowerShell Admin)
   Start-Service Docker
   
   # macOS
   open /Applications/Docker.app
   ```

2. **Vérifie installation** :
   ```bash
   docker --version
   docker-compose --version
   ```

---

### ❌ "Image not found"

```
ERROR: image not found
```

**Solutions** :

1. **Build l'image** :
   ```bash
   docker build -t gateway-front:latest .
   ```

2. **Utilise docker-compose** (build automatique) :
   ```bash
   docker-compose up --build
   ```

---

### ❌ "Container won't start"

```
docker: Error response from daemon
```

**Solutions** :

1. **Vérifiez les logs** :
   ```bash
   docker-compose logs app
   ```

2. **Nettoie les conteneurs** :
   ```bash
   docker-compose down -v
   docker-compose up -d
   ```

3. **Rebuild l'image** :
   ```bash
   docker-compose build --no-cache
   docker-compose up
   ```

---

### ❌ "Port already in use (Docker)"

```
Error response from daemon: Ports are not available
```

**Solutions** :

1. **Utilise ports différents** :
   ```yaml
   # docker-compose.yml
   ports:
     - "9090:8080"  # Host:Container
     - "5433:5432"  # Pour PostgreSQL
   ```

2. **Arrête les conteneurs** :
   ```bash
   docker-compose down
   ```

---

## Template & Thymeleaf Issues

### ❌ "Template not found"

```
Error resolving template [app-list]
```

**Solutions** :

1. **Vérifie le chemin** :
   ```
   src/main/resources/templates/app-list.html
   ```

2. **Nom exact du fichier** :
   - Fichier: `app-list.html`
   - Contrôleur retourne: `"app-list"` (pas extension)

3. **Vérifie Thymeleaf config** :
   ```properties
   spring.thymeleaf.mode=HTML
   spring.thymeleaf.encoding=UTF-8
   ```

---

### ❌ "Variable not defined"

```
Template error: variable [app] is not defined
```

**Solutions** :

```java
// AppController.java
@GetMapping("/app/{id}")
public String viewApp(@PathVariable Long id, Model model) {
    AppResponse app = appService.getAppById(id);
    model.addAttribute("app", app);  // Ajoute la variable
    return "app-detail";
}
```

---

### ❌ "CSS/JavaScript not loading"

```
GET /css/style.css HTTP 404
```

**Solutions** :

1. **Vérifie le chemin** :
   ```html
   <link rel="stylesheet" href="/css/style.css">
   <!-- Pas "static/css/..." -->
   ```

2. **Structur des resources** :
   ```
   src/main/resources/static/
   ├── css/
   │   └── style.css
   ├── js/
   │   └── app.js
   ```

3. **Redémarrage du serveur** :
   ```bash
   mvn spring-boot:run
   ```

---

## Testing Issues

### ❌ "Tests won't run"

```
Tests not found or error compiling test class
```

**Solutions** :

```bash
# Clean et recompile
mvn clean test

# Test spécifique
mvn test -Dtest=StorageServiceTest

# Voir les détails
mvn test -X
```

---

### ❌ "@WithMockUser not working"

```
Test expected ADMIN role but got ANONYMOUS
```

**Solutions** :

```java
// Ajoute dépendance
<dependency>
    <groupId>org.springframework.security</groupId>
    <artifactId>spring-security-test</artifactId>
    <scope>test</scope>
</dependency>

// Ajoute annotation
@WithMockUser(username = "admin", roles = {"ADMIN"})
void testAdminEndpoint() { ... }
```

---

## Performance Issues

### ❌ "Application is slow"

**Solutions** :

1. **Profiler** :
   ```bash
   # Activa les métriques
   management.endpoints.web.exposure.include=*
   # http://localhost:8080/actuator
   ```

2. **Réduit la log verbosité** :
   ```properties
   logging.level.root=WARN
   ```

3. **Cache les données** :
   ```java
   @Cacheable("apps")
   public List<AppResponse> getAllApps() { ... }
   ```

---

### ❌ "High memory usage"

**Solutions** :

```bash
# Réduit heap size (développement)
mvn spring-boot:run \
  -Dspring-boot.run.jvmArguments="-Xmx512m"
```

---

## General Troubleshooting Steps

1. **Check Logs** :
   ```bash
   # Maven
   mvn clean compile -X  # Verbose
   
   # Application
   tail -f logs/spring.log
   
   # Docker
   docker-compose logs -f
   ```

2. **Validate Configuration** :
   ```bash
   # H2 Console
   http://localhost:8080/h2-console
   
   # Actuator
   http://localhost:8080/actuator
   ```

3. **Clean & Rebuild** :
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Reset Everything** :
   ```bash
   # Dev
   rm -rf target/ uploads/
   mvn clean install
   
   # Docker
   docker-compose down -v
   docker-compose up --build
   ```

---

## Quick Reference

| Issue | Quick Fix |
|-------|-----------|
| Port busy | Use `--server.port=9090` |
| Maven not found | Install Maven or add to PATH |
| Java version wrong | Install Java 17, update JAVA_HOME |
| DB locked (H2) | Restart application |
| Postgres down | `docker-compose restart postgres` |
| Can't upload ZIP | File too big (>10MB) or invalid |
| 401 Unauthorized | Check credentials (admin:admin123) |
| 403 Forbidden | Check role (need ADMIN for /register) |
| Template not found | Check file location and name |
| CSS/JS not loading | Use `/css/...` not `/static/css/...` |

---

## Still Stuck?

1. **Consulte les docs** :
   - README.md
   - API_DOCUMENTATION.md
   - QUICK_START.md

2. **Regarde les logs** :
   ```bash
   mvn spring-boot:run -X
   ```

3. **Teste isolément** :
   ```bash
   mvn test -Dtest=AppServiceTest
   ```

4. **Reset complet** :
   ```bash
   docker-compose down -v
   mvn clean install
   ```

---

Besoin d'aide ? Consulte les fichiers de documentation ou ouvre une issue. 📞


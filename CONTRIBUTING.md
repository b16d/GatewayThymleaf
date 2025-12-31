# Contributing to Thymeleaf Portal

Guide de développement et contribution au projet Thymeleaf Portal.

## 🏗️ Architecture

### Couches

```
┌─────────────────────────┐
│   Thymeleaf Templates   │  (UI Layer)
│   HTML + CSS + JS       │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│      AppController      │  (Presentation Layer)
│      ProxyController    │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│     AppService          │  (Business Logic)
│     StorageService      │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│    RegisteredApp        │  (Data Layer)
│    RegisteredAppRepo    │
└────────────┬────────────┘
             │
┌────────────▼────────────┐
│   H2 (Dev) / PostgreSQL │  (Database)
│         (Prod)          │
└─────────────────────────┘
```

### Dépendances

```
GatewayFrontApplication
├── SecurityConfig (Spring Security)
├── WebConfig (Static Resources)
├── AppController
│   ├── AppService
│   │   ├── RegisteredAppRepository
│   │   └── StorageService
│   └── Model (RegisteredApp)
└── ProxyController
    └── RegisteredAppRepository
```

---

## 🛠️ Configuration IDE

### IntelliJ IDEA

1. **Ouvre le projet** : `File → Open → GatewayFront folder`
2. **Importe Maven** : Clic sur `pom.xml` → `Add as Maven Project`
3. **Configure JDK** : `File → Project Structure → Project SDK → Java 17`
4. **Activa Lombok** (optionnel) : `Settings → Plugins → Lombok`

### Configuration Build

**Menu Run → Edit Configurations :**
```
Name: Run GatewayFront
Main class: com.article.ai.gatewayfront.GatewayFrontApplication
JVM options: -Dspring.profiles.active=dev
Working directory: $MODULE_DIR$
```

---

## 📝 Conventions de Code

### Java

```java
// Package naming : com.article.ai.gatewayfront.{layer}
package com.article.ai.gatewayfront.service;

// Class naming : PascalCase
public class AppService {

    // Method naming : camelCase
    public AppResponse registerRemoteApp(...) {
        
        // Variable naming : camelCase
        String appName = "test";
        
        // Constants : UPPER_SNAKE_CASE
        private static final long MAX_FILE_SIZE = 10 * 1024 * 1024;
    }
}

// Logging
private static final Logger logger = LoggerFactory.getLogger(AppService.class);
logger.debug("Message: {}", variable);
```

### HTML/Thymeleaf

```html
<!-- Use semantic HTML5 -->
<section>
    <!-- Thymeleaf expressions -->
    <h2 th:text="${app.appName}">Default Title</h2>
    
    <!-- Conditional rendering -->
    <div th:if="${app.active}">Active</div>
    
    <!-- Loops -->
    <div th:each="app : ${apps}">
        <span th:text="${app.appName}"></span>
    </div>
</section>
```

### CSS

```css
/* BEM Naming Convention (optionnel mais recommandé) */
.app-card {
    /* Block */
}

.app-card__title {
    /* Element */
}

.app-card--active {
    /* Modifier */
}
```

---

## 🔄 Git Workflow

### Branch Naming

```
feature/feature-name          # Nouvelle fonctionnalité
bugfix/bug-description        # Correction bug
docs/documentation-topic      # Documentation
refactor/refactoring-scope    # Refactoring
chore/maintenance-task        # Tâches de maintenance
```

### Commit Messages

```
<type>(<scope>): <subject>

<body>

<footer>
```

**Types** : `feat`, `fix`, `docs`, `style`, `refactor`, `test`, `chore`

**Exemples** :
```
feat(auth): add OAuth2 support
fix(upload): validate ZIP path traversal
docs(api): add endpoint documentation
test(service): add AppService unit tests
refactor(controller): extract validation logic
```

### Pull Request Checklist

- [ ] Tests ajoutés/mis à jour
- [ ] Documentation mise à jour
- [ ] Code suivi les conventions
- [ ] Pas de warnings Maven
- [ ] Branche à jour avec main

---

## 🧪 Testing Guide

### Structure Tests

```java
@ExtendWith(MockitoExtension.class)  // Unit tests
class StorageServiceTest {
    
    @Mock
    private SomeDependency dependency;
    
    @InjectMocks
    private StorageService service;
    
    @Test
    void testMethodName() {
        // Arrange
        String input = "test";
        
        // Act
        String result = service.process(input);
        
        // Assert
        assertEquals("expected", result);
    }
}
```

```java
@SpringBootTest                       // Integration tests
@AutoConfigureMockMvc
class AppControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    @WithMockUser(roles = "ADMIN")
    void testEndpoint() throws Exception {
        mockMvc.perform(get("/api/apps"))
            .andExpect(status().isOk());
    }
}
```

### Couvrir les Cas

```java
// Pour AppService.registerRemoteApp()
void testRegisterRemoteApp_Success()           // Happy path
void testRegisterRemoteApp_DuplicateName()     // Conflict
void testRegisterRemoteApp_InvalidUrl()        // Validation
void testRegisterRemoteApp_NullParameters()    // Edge case
```

### Exécuter les Tests

```bash
# Tous les tests
mvn test

# Test spécifique
mvn test -Dtest=StorageServiceTest

# Avec couverture
mvn test jacoco:report
# Rapport : target/site/jacoco/index.html

# Skip tests (build seulement)
mvn clean package -DskipTests
```

---

## 📚 Ajouter une Nouvelle Fonctionnalité

### Exemple : Support des Authentifications OAuth

#### 1. Ajouter Dépendance

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-oauth2-client</artifactId>
</dependency>
```

#### 2. Configurer OAuth

```java
// SecurityConfig.java
@Configuration
public class SecurityConfig {
    // OAuth2 configuration
}
```

#### 3. Ajouter Endpoints

```java
// AppController.java
@GetMapping("/login/oauth2/code/google")
public ResponseEntity<?> handleOAuth2Login(...) {
    // OAuth2 handling
}
```

#### 4. Mettre à Jour UI

```html
<!-- login.html -->
<a href="/oauth2/authorization/google">Login with Google</a>
```

#### 5. Tests

```java
// Ajouter tests OAuth2SecurityTest
```

#### 6. Documentation

```markdown
<!-- README.md -->
- OAuth2 authentication support added
```
```

---

## 🐛 Debugging

### IntelliJ Debugger

1. **Ajoute Breakpoint** : Clic gauche sur numéro ligne
2. **Exécute en Debug** : `Shift+F9`
3. **Step Over** : `F10`
4. **Step Into** : `F11`
5. **Resume** : `F9`

### Logs

```java
// En développement
logger.debug("Variable: {}", variable);
logger.info("Action completed");
logger.warn("Warning message");
logger.error("Error occurred", exception);

// Voir les logs
tail -f target/logs/spring.log

# Docker logs
docker-compose logs -f app
```

### H2 Console

Accès : `http://localhost:8080/h2-console`

```sql
SELECT * FROM REGISTERED_APPS;
SELECT * FROM REGISTERED_APPS WHERE APP_NAME = 'TestApp';
DELETE FROM REGISTERED_APPS WHERE ID = 1;
```

---

## 📦 Dépendances

### Ajouter une Dépendance

```xml
<!-- pom.xml -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-mail</artifactId>
</dependency>
```

```bash
# Recharg Maven
mvn dependency:resolve

# Voir l'arbre
mvn dependency:tree

# Chercher updates
mvn versions:display-dependency-updates
```

---

## 🚀 Performance & Optimization

### Database Queries

```java
// ❌ N+1 Query Problem
List<App> apps = appRepository.findAll();
for (App app : apps) {
    app.getMetadata();  // Query pour chaque app
}

// ✅ Solution : Fetch eagerly
@OneToMany(fetch = FetchType.EAGER)
List<Metadata> metadata;

// ✅ Solution : @Query personnalisée
@Query("SELECT a FROM RegisteredApp a JOIN FETCH a.metadata")
List<RegisteredApp> findAllWithMetadata();
```

### Caching

```java
// @Cacheable
@Service
public class AppService {
    @Cacheable("apps")
    public List<AppResponse> getAllApps() {
        return appRepository.findByActive(true)
            .stream()
            .map(this::mapToResponse)
            .collect(Collectors.toList());
    }
}
```

### Compression

```java
// Gzip dans Thymeleaf
<gzip src="/static/app.css" />
```

---

## 📋 Checklist Avant Commit

- [ ] Code compile sans warnings
- [ ] Tous les tests passent
- [ ] Documentation mise à jour
- [ ] Pas de fichiers de debug
- [ ] Commits sont atomiques
- [ ] Messages commit sont clairs

---

## 🔗 Ressources Utiles

### Documentation Officielle
- [Spring Boot 3.5](https://spring.io/projects/spring-boot)
- [Spring Security](https://spring.io/projects/spring-security)
- [Thymeleaf](https://www.thymeleaf.org/)
- [JPA/Hibernate](https://hibernate.org/)

### Outils
- [Maven](https://maven.apache.org/)
- [IntelliJ IDEA](https://www.jetbrains.com/idea/)
- [Git](https://git-scm.com/)
- [Docker](https://www.docker.com/)

### Tutoriels
- Spring Boot REST APIs
- Spring Security Authentication
- Thymeleaf Template Engine
- Docker for Java Applications

---

## 📞 Questions/Support

- Consulte `README.md` pour documentation générale
- Consulte `API_DOCUMENTATION.md` pour endpoints
- Utilise les issues GitHub pour bugs/features

---

## License

MIT License - See LICENSE file

---

Heureux de contribuer ! 🎉


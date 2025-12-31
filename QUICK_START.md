# 🚀 Quick Start Guide - Thymeleaf Portal

Démarrage rapide du Thymeleaf Portal en 5 minutes.

## Mode 1 : Développement Local (Recommandé)

### Étape 1 : Clone/Ouvre le projet
```bash
cd C:\Users\Alban CLEVY\IdeaProjects\GatewayFront
```

### Étape 2 : Build
```bash
mvn clean install
```

### Étape 3 : Exécute
```bash
mvn spring-boot:run
```

Ou depuis IntelliJ :
- Clic droit sur `GatewayFrontApplication.java`
- Select `Run 'GatewayFrontApplication'`

### Étape 4 : Accès
- **Portal Home**: http://localhost:8080/
- **Login**: http://localhost:8080/login
- **Credentials**:
  - Admin: `admin` / `admin123`
  - User: `user` / `user123`

### Étape 5 : H2 Console (optionnel)
- URL : http://localhost:8080/h2-console
- JDBC URL : `jdbc:h2:mem:testdb`

---

## 🎯 Les Pages de l'Application

### 1. **Page d'Accueil** (`/`)
- 📊 Tableau de bord avec statistiques
- 🎴 Grille d'applications enregistrées
- ➕ Bouton "Enregistrer App" (ADMIN uniquement)
- 🔐 Bouton de connexion

### 2. **Formulaire d'Enregistrement** (`/register`)
**ADMIN ONLY**
- 📑 **Deux modes**:
  1. **Upload ZIP**: Téléchargez une app Thymeleaf compressée
  2. **Remote URL**: Enregistrez une app hébergée à distance
- 📝 Formulaire intuitif avec validation
- 📁 Drag-and-drop pour les fichiers ZIP

### 3. **Détails d'une Application** (`/app/{id}`)
- 📋 Informations complètes de l'app
- 🚀 Bouton pour lancer l'app
- 🗑️ Bouton de suppression (ADMIN uniquement)
- ← Retour vers le portail

### 4. **Page de Connexion** (`/login`)
- 🔐 Authentification sécurisée
- Demo credentials disponibles

---

## 🧪 Test Rapide

### Étape 1 : Accès au portail
1. Ouvre http://localhost:8080
2. Vois la liste des apps (vide au départ)

### Étape 2 : Connexion Admin
1. Clique sur **"🔐 Login"** (en haut à droite)
2. Entre: `admin` / `admin123`
3. Clique **"Login"**

### Étape 3 : Enregistre une App Remote
1. Clique **"➕ Register App"**
2. Sélectionne le tab **"🔗 Remote URL"**
3. Remplis:
   - Name: `Example App`
   - Description: `A demo application`
   - Remote URL: `https://www.thymeleaf.org`
4. Clique **"✨ Register Application"**

### Étape 4 : Vois l'app dans la liste
1. T'es automatiquement redirigé vers la home
2. Tu vois ta nouvelle app dans la grille

### Étape 5 : Accès l'app
1. Clique **"👁️ View"** sur la carte
2. Vois tous les détails
3. Clique **"🔗 Access App"** pour accéder

---

## 📁 Créer et Uploader une App Thymeleaf

### 1. Crée une app simple
```bash
# Crée la structure
mkdir -p sample-app/templates

# Crée l'index.html
cat > sample-app/templates/index.html << 'EOF'
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Sample App</title>
    <style>
        body { font-family: Arial; margin: 2rem; }
        h1 { color: #667eea; }
    </style>
</head>
<body>
    <h1>Bienvenue dans Sample App!</h1>
    <p>Ceci est une application Thymeleaf simple.</p>
</body>
</html>
EOF
```

### 2. Crée le ZIP
```bash
cd sample-app
zip -r ../sample-app.zip .
cd ..
```

### 3. Upload dans le portail
1. Clique **"➕ Register App"**
2. Sélectionne le tab **"📦 Upload ZIP"**
3. Remplis:
   - Name: `Sample App`
   - Description: `My Thymeleaf application`
4. Glisse-dépose ou clique pour charger `sample-app.zip`
5. Clique **"✨ Register Application"**

### 4. Lance l'app
1. Retour à la home
2. Clique **"👁️ View"** sur ta nouvelle app
3. Clique **"🚀 Launch App"** pour l'ouvrir

---

## Mode 2 : Docker Compose (Full Stack)

### Étape 1 : Assure-toi d'avoir Docker
```bash
docker --version
docker-compose --version
```

### Étape 2 : Lance les services
```bash
docker-compose up -d
```

### Étape 3 : Attends ~30s pour le démarrage
```bash
docker-compose logs -f app
```

### Étape 4 : Accès
- Portal: http://localhost:8080
- PostgreSQL: localhost:5432
- Login: `admin` / `admin123`

---

## 🎨 Caractéristiques de l'UI

✨ **Design Moderne**
- Gradient background (purple)
- Animations fluides
- Design responsive
- Support mobile complet

🎯 **Fonctionnalités**
- Statistiques en temps réel
- Grille d'apps avec cards
- Formulaire d'enregistrement intuitif
- Gestion complète des apps

🔒 **Sécurité**
- Authentification Spring Security
- Rôles ADMIN/USER
- CSRF protection
- Validation des inputs

---

## 📚 Documentation Complète

- **Pages Guide**: Voir [PAGE_GUIDE.md](PAGE_GUIDE.md)
- **API**: Voir [API_DOCUMENTATION.md](API_DOCUMENTATION.md)
- **Configuration**: Voir [CONFIGURATION.md](CONFIGURATION.md)
- **Troubleshooting**: Voir [TROUBLESHOOTING.md](TROUBLESHOOTING.md)

---

## Credentials Demo

| User | Password | Roles |
|------|----------|-------|
| `admin` | `admin123` | ADMIN, USER |
| `user` | `user123` | USER |

---

## Problèmes Courants

### App ne démarre pas
```bash
# Vérifie Java
java -version

# Vérifie Maven
mvn -version

# Vérifie le port 8080
lsof -i :8080
```

### Port 8080 occupé
```bash
# Tue le processus
lsof -ti:8080 | xargs kill -9
```

### Base de données
- H2 en mémoire (reset au redémarrage)
- PostgreSQL en Docker Compose

---

**À bientôt dans le Thymeleaf Portal! 🎉**

---

## Mode 2 : Docker Compose (Full Stack)

### Étape 1 : Assure-toi d'avoir Docker
```bash
docker --version
docker-compose --version
```

### Étape 2 : Lance les services
```bash
docker-compose up -d
```

### Étape 3 : Attends ~30s pour le démarrage
```bash
docker-compose logs -f app
```

### Étape 4 : Accès
- URL : **http://localhost:8080/**
- Login : `admin` / `admin123`
- DB : PostgreSQL sur `localhost:5432`

### Étape 5 : Arrête les services
```bash
docker-compose down
```

---

## Mode 3 : Développement avec PostgreSQL externe

### Étape 1 : Lance juste PostgreSQL
```bash
docker-compose -f docker-compose.postgres-only.yml up -d
```

### Étape 2 : Lance l'app en mode production
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--spring.profiles.active=prod"
```

### Étape 3 : Accès
- URL : **http://localhost:8080/**
- Login : `admin` / `admin123`

---

## Test Rapide API

### Enregistre une app distante
```bash
curl -X POST http://localhost:8080/api/apps/register/remote \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "TestApp",
    "description": "App de test",
    "remoteBaseUrl": "https://www.thymeleaf.org"
  }'
```

### Récupère toutes les apps
```bash
curl -X GET http://localhost:8080/api/apps \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

### Supprime une app
```bash
curl -X DELETE http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

---

## Authentifiants de Démo

| Rôle | Username | Password |
|------|----------|----------|
| Admin | `admin` | `admin123` |
| User | `user` | `user123` |

---

## Créer un Example ZIP

Sur macOS/Linux :
```bash
bash create-sample-app.sh
# Crée : sample-app.zip
```

Puis upload via :
```bash
curl -X POST http://localhost:8080/api/apps/register/upload \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -F "appName=SampleApp" \
  -F "description=App exemple" \
  -F "zipFile=@sample-app.zip"
```

---

## Troubleshooting

### Port 8080 busy
```bash
netstat -ano | findstr :8080  # Windows
lsof -i :8080                  # macOS/Linux
```

Utilise un autre port :
```bash
mvn spring-boot:run -Dspring-boot.run.arguments="--server.port=9090"
```

### Maven pas trouvé
Assure-toi que Maven est installé :
```bash
mvn --version
```

### Tests échouent
```bash
mvn clean test -DskipTests=false
```

### Docker erreurs
```bash
# Logs
docker-compose logs -f

# Clean & restart
docker-compose down -v
docker-compose up -d
```

---

## Fichiers Importants

| Fichier | Purpose |
|---------|---------|
| `pom.xml` | Dépendances Maven |
| `src/main/java/...` | Code source Java |
| `src/main/resources/templates/` | Templates Thymeleaf |
| `application.properties` | Config dev (H2) |
| `application-prod.properties` | Config prod (PostgreSQL) |
| `Dockerfile` | Build image Docker |
| `docker-compose.yml` | Stack Docker complète |

---

## Commandes Utiles

```bash
# Build sans tests
mvn clean package -DskipTests

# Exécute les tests
mvn test

# Compile seulement
mvn clean compile

# Nettoie
mvn clean

# Affiche les dépendances
mvn dependency:tree

# Met à jour les dépendances
mvn versions:display-dependency-updates
```

---

## Prochaines Étapes

1. **Connecte-toi** avec admin
2. **Enregistre une app distante** (clique sur "Register App")
3. **Visite le portail d'accueil** pour voir l'app
4. **Upload un ZIP** pour une app uploadée
5. **Teste l'API** avec les commandes curl

---

## Besoin d'aide ?

- Consulte `README.md` pour doc complète
- Consulte `API_DOCUMENTATION.md` pour endpoints REST
- Vérifie les logs :
  ```bash
  # Dev
  tail -f target/logs/spring.log
  
  # Docker
  docker-compose logs -f app
  ```

---

**Bon développement ! 🎉**


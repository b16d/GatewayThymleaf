# 🧪 Guide de Test - Application Nettoyée

## ✅ Checklist de Vérification

### 1️⃣ **Démarrage de l'Application**

```bash
cd C:\Users\Alban CLEVY\IdeaProjects\GatewayFront
mvn clean install
mvn spring-boot:run
```

Vérifications:
- ✅ Pas d'erreur de compilation
- ✅ Pas d'erreur de démarrage
- ✅ Pas d'erreur "data.sql"
- ✅ Application démarre sur port 8080

### 2️⃣ **Page d'Accueil Sans Authentification**

**URL**: `http://localhost:8080/`

Vérifications:
- ✅ Page affichée (liste vide)
- ✅ Bouton "🔐 Login" visible
- ✅ Bouton "➕ Register App" **INVISIBLE**
- ✅ Message "No Applications Yet"

### 3️⃣ **Authentification**

**URL**: `http://localhost:8080/login`

Vérifications:
- ✅ Page login accessible
- ✅ Credentials:
  - Username: `admin`
  - Password: `admin123`
- ✅ Login successful → Redirection vers `/`

### 4️⃣ **Page d'Accueil Après Login (ADMIN)**

**URL**: `http://localhost:8080/`

Vérifications:
- ✅ Bouton "➕ Register App" **VISIBLE**
- ✅ Bouton "🔐 Login" **INVISIBLE**
- ✅ Bouton "🔓 Logout" **VISIBLE**

### 5️⃣ **Page d'Enregistrement**

**URL**: `http://localhost:8080/register`

Vérifications:
- ✅ Formulaire simplifié (3 champs):
  - Application Name (texte)
  - Description (textarea)
  - Remote Base URL (URL)
- ✅ **PAS de** drag-and-drop ZIP
- ✅ **PAS de** tabs/onglets
- ✅ **PAS de** champ "ZIP file"

### 6️⃣ **Enregistrer une Application**

Remplir le formulaire:
```
Name: "Ma Première App"
Description: "Mon application test"
URL: "http://localhost:9090"
```

Vérifications:
- ✅ Message "App registered successfully"
- ✅ Redirection vers `/`
- ✅ App apparait dans la liste
- ✅ Type: "REMOTE" (vert)

### 7️⃣ **Accéder à l'Application**

Clic sur "🚀 Access App":
- ✅ Nouvelle tab ouverte
- ✅ URL: `http://localhost:9090` 
- ✅ Application affichée

### 8️⃣ **Page de Détails**

Clic sur l'app (ou Détails):
- ✅ Titre, description, type
- ✅ Bouton "← Back to Portal"
- ✅ Bouton "🚀 Access App"
- ✅ Bouton "🗑️ Delete" (ADMIN only)

### 9️⃣ **Supprimer une Application**

Clic sur "🗑️ Delete":
- ✅ App supprimée
- ✅ Redirection vers `/`
- ✅ App plus dans la liste

### 🔟 **Logout**

Clic sur "🔓 Logout":
- ✅ Redirection vers `/login`
- ✅ Session fermée
- ✅ Bouton "Register App" **INVISIBLE** (à nouveau)

---

## 🛡️ Tests de Sécurité

### Test 1: Non-ADMIN ne peut pas accéder /register
- Se login avec utilisateur NON-ADMIN (si existe)
- ✅ Redirect vers `/login` si accès direct à `/register`

### Test 2: Non-authentifié ne voit pas Register
- Sans login
- ✅ Pas de bouton "Register App" sur home
- ✅ Clic sur `/register` → redirect `/login`

### Test 3: Logout fonctionne correctement
- Après logout
- ✅ Bouton Register disparait
- ✅ Bouton Login réapparait

---

## 📊 Points de Vérification Important

| Point | Avant (Attendu) | Après Login | Après Logout |
|-------|-----------------|-------------|--------------|
| Bouton Register | ❌ Invisible | ✅ Visible | ❌ Invisible |
| Bouton Login | ✅ Visible | ❌ Invisible | ✅ Visible |
| Bouton Logout | ❌ Invisible | ✅ Visible | ❌ Invisible |
| Accès /register | ❌ Redirigé | ✅ Accessible | ❌ Redirigé |

---

## 🔍 Logs à Vérifier

Dans la console:
- ✅ Pas de `RegisteredApp.AppType.UPLOADED`
- ✅ Pas d'erreur `"appType"` 
- ✅ Pas de "ZIP" ou "upload"
- ✅ Pas d'erreur `data.sql`

---

## ✨ Cas d'Erreur Possibles

### Erreur: "Invalid app type"
- ❌ Problème: Type non reconnu
- ✅ Solution: Vérifier que contrôleur envoie "REMOTE"

### Erreur: "data.sql" au démarrage
- ❌ Problème: Fichier pas vide
- ✅ Solution: Vérifier `spring.sql.init.mode=never` dans properties

### Bouton Register apparait pour non-ADMIN
- ❌ Problème: Sécurité non appliquée
- ✅ Solution: Vérifier `sec:authorize="hasRole('ADMIN')"`

---

## 🚀 Tests API (optionnel)

```bash
# Enregistrer une app via API
curl -X POST http://localhost:8080/api/apps/register/remote \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "API Test App",
    "description": "App créée via API",
    "remoteBaseUrl": "http://localhost:9090"
  }'

# Lister les apps
curl http://localhost:8080/api/apps

# Voir une app
curl http://localhost:8080/api/apps/1

# Supprimer une app
curl -X DELETE http://localhost:8080/api/apps/1
```

---

## ✅ Checklist Finale

- [ ] Application démarre sans erreur
- [ ] Home page sans Register (non-auth)
- [ ] Login fonctionne avec admin/admin123
- [ ] Register App visible après login ADMIN
- [ ] Formulaire simplifié (URL only)
- [ ] Enregistrer une app fonctionne
- [ ] App apparait dans la liste
- [ ] Bouton Access App fonctionne
- [ ] Logout fonctionne
- [ ] Register App disparait après logout

---

**Si tous les points sont ✅, le nettoyage est réussi!** 🎉


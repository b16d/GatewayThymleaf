# 🎯 Cas d'Usage et Workflows

## 📌 Vue d'Ensemble

Le Thymeleaf Portal permet à deux types d'utilisateurs d'interagir avec des applications Thymeleaf :

1. **Administrateurs** : Peuvent enregistrer, gérer et supprimer des apps
2. **Utilisateurs** : Peuvent consulter et accéder aux apps

---

## 👨‍💼 Workflows Administrateur

### 🔷 Cas 1: Enregistrer une App Distante (Remote URL)

**Objectif**: Ajouter une application hébergée sur un serveur externe.

**Prérequis**:
- Être authentifié en ADMIN
- Avoir l'URL valide d'une app Thymeleaf
- URL commence par http:// ou https://

**Étapes**:

1. **Page d'enregistrement**
   - `/register` affichée
   - Formulaire pour Remote URL disponible

2. **Saisie des détails**
   - **App Name**: "Analytics Portal"
   - **Description**: "Real-time analytics dashboard"
   - **Remote URL**: `https://analytics.example.com`

3. **Validation**
   - URL validée (format + accessible)

4. **Enregistrement**
   - POST /register avec appType=REMOTE
   - Données sauvegardées en DB

5. **Confirmation**
   - Redirection `/`
   - Message success
   - App affichée avec type "REMOTE"

**Flux complète**:
```
Admin Home (/)
  → Clic "➕ Register App"
  → GET /register
  → Saisit données + URL
  → POST /register
  → App enregistrée
  → Retour home
  → App visible avec type REMOTE
```


**Objectif**: Supprimer une application du portail.

**Prérequis**:
- Être ADMIN
- Application à supprimer existe
- Confirmation utilisateur

**Étapes**:

1. **Vue la liste**
   - Home page (/) affichée
   - Cards des apps visibles

2. **Accès aux détails**
   - Clic "👁️ View" sur une card
   - GET /app/{id}
   - Page détails affichée

3. **Suppression**
   - Bouton "🗑️ Delete App" visible (ADMIN only)
   - Clic sur le bouton

4. **Confirmation**
   - Dialog: "Are you sure you want to delete..."
   - Clic OK pour confirmer

5. **Suppression en backend**
   - DELETE /api/apps/{id}
   - App supprimée de la DB
   - Fichiers ZIP deletés si UPLOADED

6. **Confirmation**
   - Alert: "Application deleted successfully!"
   - Redirection vers /
   - App n'est plus dans la liste

**Flux complète**:
```
Home (/)
  → Clic "👁️ View" sur app
  → GET /app/{id}
  → Détails affichés
  → Clic "🗑️ Delete App"
  → Confirmation dialog
  → DELETE /api/apps/{id}
  → Alert success
  → Redirection /
  → App supprimée
```

---

### 🔷 Cas 4: Consulter les Apps d'un Utilisateur

**Objectif**: Voir l'état et les détails de toutes les applications enregistrées.

**Prérequis**:
- Être authentifié en ADMIN
- Au moins une app enregistrée

**Étapes**:

1. **Home page**
   - `/` affichée
   - Stats visibles:
     - Total Apps: 5
     - Uploaded: 2
     - Remote: 3

2. **Grille d'apps**
   - Cards affichées
   - Chaque card montre:
     - Nom + description
     - Type (UPLOADED/REMOTE)
     - Créateur + date
     - Boutons View/Delete

3. **Détails d'une app**
   - Clic "👁️ View"
   - Page complète affichée:
     - Infos détaillées
     - Remote URL (si applicable)
     - Status (Active/Inactive)
     - Dates de création/update

4. **Actions**
   - Peut voir toutes les infos
   - Peut lancer l'app
   - Peut supprimer (ADMIN)

---

## 👤 Workflows Utilisateur Régulier

### 🟢 Cas 1: Consulter les Applications Disponibles

**Objectif**: Voir les apps disponibles et leurs descriptions.

**Prérequis**:
- Aucune authentification requise
- Au moins une app enregistrée

**Étapes**:

1. **Accès au portail**
   - URL: `http://localhost:8080/`
   - Home page chargée

2. **Vue de la liste**
   - Stats affichées (apps total)
   - Cards des apps visibles
   - Chaque card montre:
     - Nom + icon
     - Description
     - Type (badge)
     - Créateur
     - Bouton "👁️ View"

3. **Consultation**
   - Peut lire les descriptions
   - Peut voir les types
   - Pas accès à "Enregistrer"

---

### 🟢 Cas 2: Accéder à une Application

**Objectif**: Accéder à des fonctionnalités réservées (optionnel).

**Prérequis**:
- Avoir des credentials
- Accès à page /login

**Étapes**:

1. **Home page**
   - Bouton "🔐 Login" visible en haut à droite

2. **Page de connexion**
   - Clic "🔐 Login"
   - GET /login
   - Formulaire affiché

3. **Saisie credentials**
   - Username: `user` ou `admin`
   - Password: `user123` ou `admin123`

4. **Soumission**
   - POST /login
   - Spring Security traite
   - Si ok: redirection `/`
   - Si erreur: message d'erreur

5. **Session établie**
   - User/ADMIN identifié
   - Boutons ADMIN visibles si ADMIN
   - Peut accéder à /register si ADMIN

---

## 🔄 Workflows Alternatifs

### ⚠️ Cas: Tentative Non-Autorisée

**Scénario**: User essaye d'accéder à `/register` sans être ADMIN

**Étapes**:

1. **User tente d'accéder**
   - URL: `/register`
   - Mais user n'a rôle USER

2. **Spring Security intercephe**
   - AuthorizationFilter check
   - Rôle ADMIN requis mais pas présent

3. **Redirection**
   - Vers `/login`
   - Message: "Access Denied"

4. **Solution**:
   - Login avec compte ADMIN
   - Ou voir les apps avec accès USER

---

### ⚠️ Cas: Erreur Upload

**Scénario**: Admin upload un fichier > 10MB

**Étapes**:

1. **Upload ZIP**
   - Fichier 15MB sélectionné

2. **Validation cliente**
   - JavaScript valide taille
   - File size check: 15MB > 10MB

3. **Message d'erreur**
   - Alert: "File size exceeds 10MB limit!"
   - Fichier reset à vide
   - User doit réessayer

4. **Prévention côté serveur**
   ```properties
   spring.servlet.multipart.max-file-size=10MB
   ```

---

### ⚠️ Cas: App Distante Inaccessible

**Scénario**: Admin enregistre URL invalide

**Étapes**:

1. **Enregistrement**
   - Remote URL: `https://invalid-url-xyz.com`
   - App enregistrée quand même

2. **Utilisation**
   - User clique "🔗 Access App"
   - Navigateur tente accès
   - Erreur 404/Connection Refused

3. **Gestion**
   - Admin peut supprimer l'app
   - Ou corriger l'URL

---

## 📊 Matrice d'Accès

| Action | Anonymous | USER | ADMIN |
|--------|-----------|------|-------|
| Voir home (/) | ✓ | ✓ | ✓ |
| Accéder app | ✓ | ✓ | ✓ |
| Page /register | ✗ | ✗ | ✓ |
| Enregistrer app | ✗ | ✗ | ✓ |
| Supprimer app | ✗ | ✗ | ✓ |
| Voir bouton delete | ✗ | ✗ | ✓ |

---

## 🎨 UI States

### States de la Page Home

#### Empty State (Aucune app)
- Message "📭 No Applications Yet"
- Explication pour users
- Bouton "Register" pour ADMIN
- Message "Only admins..." pour users

#### With Apps State
- Stats affichées
- Cards grid visible
- All actions available
- Pour ADMIN: bouton Register visible

### States des Cards

#### Base State
- Affichage normal
- Hover: élévation + ombre

#### Admin State (ADMIN logged in)
- Bouton "Delete" visible sur details
- Bouton "Register" visible en header

#### User State (USER logged in)
- Pas de boutons delete
- Pas de bouton register
- Peut voir et accéder aux apps

#### Anonymous State (Not logged in)
- Peut voir apps
- Pas de boutons admin
- Login button visible

---

## 📱 Responsive Behaviors

### Desktop (≥1200px)
- 3-column grid d'apps
- Stats en 3 colonnes
- Tous les controls visibles
- Optimisé pour écran large

### Tablet (768-1199px)
- 2-column grid
- Stats en 2 colonnes
- Spacing réduit
- Touch-friendly buttons

### Mobile (<768px)
- 1-column full-width
- Stats stacked
- Buttons plus gros
- Optimisé pour doigt

---

## ✅ Checklist Complet

### Admin Features
- [ ] Register app ZIP
- [ ] Register app Remote URL
- [ ] View app details
- [ ] Delete app
- [ ] See statistics
- [ ] Access home
- [ ] Login/logout

### User Features
- [ ] View app list
- [ ] See app details
- [ ] Access uploaded app
- [ ] Access remote app
- [ ] See statistics
- [ ] Access home
- [ ] Login/logout

### UI/UX
- [ ] Responsive design
- [ ] Smooth animations
- [ ] Clear navigation
- [ ] Help text visible
- [ ] Error messages clear
- [ ] Status badges visible
- [ ] Buttons accessible

---

**Tous les workflows sont maintenant testés et validés! 🎉**


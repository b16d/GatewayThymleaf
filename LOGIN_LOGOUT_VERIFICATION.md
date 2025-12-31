# ✅ Vérification - Bouton Login et Logout

## 🎯 Fonctionnement Correct

Avec la dépendance Thymeleaf Security ajoutée, voici comment ça doit fonctionner :

---

## 📋 Checklist de Vérification

### 1️⃣ **Page d'Accueil SANS Login**

**URL**: `http://localhost:8080/`

**Doit voir**:
```
✅ Bouton "🔐 Login"      ← VISIBLE (pour se connecter)
❌ Bouton "🔓 Logout"     ← INVISIBLE
❌ Bouton "➕ Register"    ← INVISIBLE
```

**Action**: Clique sur "🔐 Login"

---

### 2️⃣ **Page de Login**

**URL**: `http://localhost:8080/login`

**Remplir**:
```
Email:    admin
Password: admin123
```

**Action**: Clique "Sign In"

---

### 3️⃣ **Page d'Accueil APRÈS Login (ADMIN)**

**URL**: `http://localhost:8080/` (redirection automatique après login)

**Doit voir**:
```
❌ Bouton "🔐 Login"      ← INVISIBLE (déjà connecté)
✅ Bouton "🔓 Logout"     ← VISIBLE (pour se déconnecter)
✅ Bouton "➕ Register"    ← VISIBLE (ADMIN peut enregistrer)
```

**Actions disponibles**:
- Clic "➕ Register" → Enregistrer une app
- Clic "🔓 Logout" → Se déconnecter

---

### 4️⃣ **Après Logout**

**Action**: Clique "🔓 Logout"

**Redirection**: Vers `/login`

**Retour à l'accueil** `http://localhost:8080/`:
```
✅ Bouton "🔐 Login"      ← VISIBLE à nouveau
❌ Bouton "🔓 Logout"     ← INVISIBLE à nouveau
❌ Bouton "➕ Register"    ← INVISIBLE à nouveau
```

---

## 🔄 Cycle Complet

```
┌──────────────────────────────────┐
│  HOME PAGE (Non authentifié)     │
│                                  │
│  Boutons:                        │
│  ✅ Login Visible                │
│  ❌ Logout Invisible             │
│  ❌ Register Invisible           │
└────────────────┬─────────────────┘
                 │
        Clic "Login"
                 │
                 ▼
┌──────────────────────────────────┐
│  LOGIN PAGE                      │
│  admin / admin123                │
│  [Sign In]                       │
└────────────────┬─────────────────┘
                 │
        Login Successful
                 │
                 ▼
┌──────────────────────────────────┐
│  HOME PAGE (Authentifié ADMIN)   │
│                                  │
│  Boutons:                        │
│  ❌ Login Invisible              │
│  ✅ Logout Visible              │
│  ✅ Register Visible            │
└────────────────┬─────────────────┘
                 │
        Clic "Logout"
                 │
                 ▼
┌──────────────────────────────────┐
│  HOME PAGE (Non authentifié)     │
│  [Boucle recommence]             │
└──────────────────────────────────┘
```

---

## 🛠️ Si Ça Ne Fonctionne Pas

### Symptôme: Bouton Logout visible même sans login

**Solution**:
1. Vérifier que `pom.xml` contient:
   ```xml
   <dependency>
       <groupId>org.thymeleaf.extras</groupId>
       <artifactId>thymeleaf-extras-spring-security6</artifactId>
   </dependency>
   ```

2. Relancer:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

3. Vider le cache du navigateur (Ctrl+Shift+Delete)

### Symptôme: Bouton Login ne s'affiche pas

**Solution**:
1. Vérifier le HTML: `sec:authorize access="!isAuthenticated()"`
2. Vérifier que le namespace est déclaré: `xmlns:sec="http://www.thymeleaf.org/extras/spring-security"`
3. Relancer l'application

---

## 📝 Code HTML Correct

Le code dans `app-list.html` doit être:

```html
<!-- Login Button - Only if NOT authenticated -->
<sec:authorize access="!isAuthenticated()">
    <a href="/login" class="btn btn-secondary">
        🔐 Login
    </a>
</sec:authorize>

<!-- Logout Button - Only if authenticated -->
<sec:authorize access="isAuthenticated()">
    <form action="/logout" method="POST" style="display: inline; margin: 0;">
        <input type="hidden" name="_csrf" th:value="${_csrf.token}"/>
        <button type="submit" class="btn btn-secondary" style="margin: 0;">
            🔓 Logout
        </button>
    </form>
</sec:authorize>
```

---

## ✨ Points Importants

1. **Bouton Login = Visible si NON connecté**
   - Expression: `!isAuthenticated()` (le `!` = NON)
   - Permet à l'utilisateur de se connecter

2. **Bouton Logout = Visible si connecté**
   - Expression: `isAuthenticated()`
   - Permet à l'utilisateur de se déconnecter

3. **Sécurité CSRF**
   - Token inclus dans le formulaire logout
   - `name="_csrf"` + `th:value="${_csrf.token}"`

---

## 🧪 Test Rapide (5 minutes)

```bash
# 1. Compiler
mvn clean install

# 2. Lancer
mvn spring-boot:run

# 3. Sans login
# http://localhost:8080/
# ✓ Voir Login
# ✗ Pas de Logout

# 4. Login
# http://localhost:8080/login
# admin / admin123

# 5. Après login
# http://localhost:8080/
# ✗ Pas de Login
# ✓ Voir Logout

# 6. Logout
# Clic Logout
# ✓ Retour Login visible
```

---

## ✅ Résumé

| État | Login Visible | Logout Visible |
|------|---------------|----------------|
| Non connecté | ✅ OUI | ❌ NON |
| Connecté | ❌ NON | ✅ OUI |

**C'est correct!** ✅ Vous pouvez maintenant:
- ✅ Voir le bouton Login si vous n'êtes pas connecté
- ✅ Cliquer pour vous connecter
- ✅ Voir le bouton Logout si vous êtes connecté
- ✅ Cliquer pour vous déconnecter

---

**Test et confirmez que tout fonctionne!** 🚀


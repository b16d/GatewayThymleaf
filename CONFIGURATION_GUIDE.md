# Configuration du Portail - Guide d'Utilisation

## 📋 Variables de Configuration

Les paramètres de branding de l'application peuvent être configurés dans `application.properties` :

### Application Branding

```properties
# Titre du portail
app.title=🌐 Thymeleaf Portal

# Description du portail
app.description=Manage and access your Thymeleaf applications
```

## 🔄 Comment Ça Fonctionne

### Architecture

1. **application.properties**
   - Définit les valeurs de configuration
   ```properties
   app.title=🌐 Thymeleaf Portal
   app.description=Manage and access your Thymeleaf applications
   ```

2. **AppProperties.java** (Classe de Configuration)
   - Classe annotée avec `@ConfigurationProperties(prefix = "app")`
   - Charge automatiquement les propriétés depuis `application.properties`
   - Fournit les getters/setters pour accéder aux propriétés

3. **AppController.java** (Injection de Dépendances)
   - Injecte `AppProperties` via le constructeur
   - Passe les propriétés au modèle (Model)
   ```java
   model.addAttribute("appTitle", appProperties.getTitle());
   model.addAttribute("appDescription", appProperties.getDescription());
   ```

4. **app-list.html** (Template Thymeleaf)
   - Utilise les variables du modèle
   ```html
   <h1 th:text="${appTitle}">🌐 Thymeleaf Portal</h1>
   <p th:text="${appDescription}">Manage and access your Thymeleaf applications</p>
   ```

## ✏️ Pour Modifier les Valeurs

Simplement changer les valeurs dans `application.properties` :

```properties
# Exemple 1: Portal français
app.title=🌐 Portail Thymeleaf
app.description=Gérez et accédez à vos applications Thymeleaf

# Exemple 2: Portal minimaliste
app.title=App Portal
app.description=Application Management Hub

# Exemple 3: Avec emoji personnalisé
app.title=⚡ Dynamic Portal
app.description=Manage your frontend applications with ease
```

Les changements seront appliqués automatiquement au redémarrage de l'application.

## 📦 Configuration par Profil

Pour avoir des configurations différentes par environnement :

### application-dev.properties
```properties
app.title=🌐 Thymeleaf Portal (DEV)
app.description=Development Environment
```

### application-prod.properties
```properties
app.title=🌐 Thymeleaf Portal
app.description=Production Environment
```

Puis sélectionner le profil :
```properties
spring.profiles.active=prod
```

## 🎯 Avantages

✅ **Centralisation** : Toutes les configurations au même endroit
✅ **Flexibilité** : Changer sans modifier le code
✅ **Maintenabilité** : Facile à modifier et à suivre
✅ **Multi-environnement** : Configurations différentes par profil
✅ **Type-safe** : Les propriétés sont typées (String, int, etc.)

## 🔍 Fichiers Impliqués

1. `src/main/resources/application.properties` - Configuration
2. `src/main/java/com/article/ai/gatewayfront/config/AppProperties.java` - Classe de mapping
3. `src/main/java/com/article/ai/gatewayfront/controller/AppController.java` - Injection et passage au modèle
4. `src/main/resources/templates/app-list.html` - Affichage dynamique

---

**Prêt à personnaliser votre portail !** ✨


# API Documentation - Thymeleaf Portal

## Vue d'ensemble

L'API REST du Thymeleaf Portal permet de gérer les applications enregistrées de manière programmatique. Tous les endpoints requièrent une authentification HTTP Basic avec les rôles appropriés.

## Authentification

Utilisez l'authentification HTTP Basic avec un en-tête `Authorization` :

```bash
Authorization: Basic <base64(username:password)>
```

Exemple avec `admin:admin123` (base64 = `YWRtaW46YWRtaW4xMjM=`) :

```bash
curl -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" http://localhost:8080/api/apps
```

## Endpoints

### 1. Enregistrer une application uploadée

**Endpoint** : `POST /api/apps/register/upload`  
**Rôle requis** : `ADMIN`  
**Content-Type** : `multipart/form-data`

**Paramètres** :
| Nom | Type | Requis | Description |
|-----|------|--------|-------------|
| `appName` | String | Oui | Nom unique de l'application |
| `description` | String | Oui | Description de l'application |
| `zipFile` | File | Oui | Fichier ZIP contenant templates et resources |

**Réponse (201 Created)** :
```json
{
  "id": 1,
  "appName": "MyApp",
  "description": "Ma première application",
  "appType": "REMOTE",
  "remoteBaseUrl": "http://localhost:9090",
  "createdAt": "2025-12-30T10:30:00",
  "createdBy": "admin",
  "active": true
}
```

**Exemple** :
```bash
curl -X POST http://localhost:8080/api/apps/register/upload \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -F "appName=MyThymeleafApp" \
  -F "description=Application de test" \
  -F "zipFile=@myapp.zip"
```

**Erreurs** :
- `400 Bad Request` : ZIP vide, mauvais format ou fichier invalide
- `409 Conflict` : Nom d'app déjà existant

---

### 2. Enregistrer une application distante

**Endpoint** : `POST /api/apps/register/remote`  
**Rôle requis** : `ADMIN`  
**Content-Type** : `application/json`

**Body** :
```json
{
  "appName": "RemoteApp",
  "description": "Application hébergée externalement",
  "remoteBaseUrl": "https://example.com/myapp"
}
```

**Champs** :
| Champ | Type | Requis | Description |
|-------|------|--------|-------------|
| `appName` | String | Oui | Nom unique de l'application |
| `description` | String | Oui | Description de l'application |
| `remoteBaseUrl` | String | Oui | URL de base de l'application (doit commencer par http:// ou https://) |

**Réponse (201 Created)** :
```json
{
  "id": 2,
  "appName": "RemoteApp",
  "description": "Application hébergée externellement",
  "appType": "REMOTE",
  "remoteBaseUrl": "https://example.com/myapp",
  "createdAt": "2025-12-30T11:45:00",
  "createdBy": "admin",
  "active": true
}
```

**Exemple** :
```bash
curl -X POST http://localhost:8080/api/apps/register/remote \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "ExternalDashboard",
    "description": "Dashboard hébergé",
    "remoteBaseUrl": "https://dashboard.example.com"
  }'
```

**Erreurs** :
- `400 Bad Request` : URL vide ou format invalide
- `409 Conflict` : Nom d'app déjà existant

---

### 3. Récupérer toutes les applications

**Endpoint** : `GET /api/apps`  
**Rôle requis** : `ADMIN`  
**Paramètres** : Aucun

**Réponse (200 OK)** :
```json
[
  {
    "id": 1,
    "appName": "MyApp",
    "description": "Ma première application",
    "appType": "REMOTE",
    "remoteBaseUrl": "http://localhost:9090",
    "createdAt": "2025-12-30T10:30:00",
    "createdBy": "admin",
    "active": true
  },
  {
    "id": 2,
    "appName": "RemoteApp",
    "description": "Application distante",
    "appType": "REMOTE",
    "remoteBaseUrl": "https://example.com",
    "createdAt": "2025-12-30T11:45:00",
    "createdBy": "admin",
    "active": true
  }
]
```

**Exemple** :
```bash
curl -X GET http://localhost:8080/api/apps \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

---

### 4. Récupérer une application par ID

**Endpoint** : `GET /api/apps/{id}`  
**Rôle requis** : `ADMIN`

**Paramètres de chemin** :
| Nom | Type | Description |
|-----|------|-------------|
| `id` | Long | ID de l'application |

**Réponse (200 OK)** :
```json
{
  "id": 1,
  "appName": "MyApp",
  "description": "Ma première application",
  "appType": "REMOTE",
  "remoteBaseUrl": "http://localhost:9090",
  "createdAt": "2025-12-30T10:30:00",
  "createdBy": "admin",
  "active": true
}
```

**Exemple** :
```bash
curl -X GET http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

**Erreurs** :
- `404 Not Found` : Application non trouvée

---

### 5. Supprimer une application

**Endpoint** : `DELETE /api/apps/{id}`  
**Rôle requis** : `ADMIN`

**Paramètres de chemin** :
| Nom | Type | Description |
|-----|------|-------------|
| `id` | Long | ID de l'application |

**Réponse (200 OK)** :
```json
{
  "message": "App deleted successfully"
}
```

**Exemple** :
```bash
curl -X DELETE http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

**Comportement** :
- Pour les applications uploadées : supprime les fichiers stockés
- Pour les applications distantes : supprime uniquement la métadonnée

**Erreurs** :
- `404 Not Found` : Application non trouvée
- `500 Internal Server Error` : Erreur lors de la suppression des fichiers

---

## Codes de Statut HTTP

| Code | Description |
|------|-------------|
| `200 OK` | Requête réussie |
| `201 Created` | Ressource créée avec succès |
| `400 Bad Request` | Paramètres invalides ou malformés |
| `401 Unauthorized` | Authentification requise ou invalide |
| `403 Forbidden` | Rôle insuffisant |
| `404 Not Found` | Ressource non trouvée |
| `409 Conflict` | Conflit (ex: nom d'app existant) |
| `500 Internal Server Error` | Erreur serveur |

---

## Modèles de Données

### RegisteredApp (Réponse)

```json
{
  "id": 1,
  "appName": "string",
  "description": "string",
  "appType": "REMOTE",
  "remoteBaseUrl": "string",
  "createdAt": "ISO 8601 DateTime",
  "createdBy": "string",
  "active": "boolean"
}
```

---

## Exemples Complets

### Workflow complet

```bash
# 1. Enregistrer une app distante
curl -X POST http://localhost:8080/api/apps/register/remote \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM=" \
  -H "Content-Type: application/json" \
  -d '{
    "appName": "GitHub",
    "description": "Accès à GitHub",
    "remoteBaseUrl": "https://github.com"
  }'

# Réponse :
# { "id": 1, "appName": "GitHub", ... }

# 2. Récupérer toutes les apps
curl -X GET http://localhost:8080/api/apps \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="

# 3. Récupérer l'app créée
curl -X GET http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="

# 4. Supprimer l'app
curl -X DELETE http://localhost:8080/api/apps/1 \
  -H "Authorization: Basic YWRtaW46YWRtaW4xMjM="
```

---

## Notes de Sécurité

- Tous les endpoints requièrent l'authentification
- Les admins seuls peuvent enregistrer et supprimer des apps
- Les URLs distantes sont validées
- Les fichiers ZIP sont inspectés pour prévenir les attaques de traversée de répertoires
- Limite de taille : 10MB par fichier

---

## Rate Limiting

Pas de rate limiting actuellement implémenté. En production, envisagez d'ajouter une limitation basée sur IP ou utilisateur.

---

## Versioning de l'API

Version actuelle : `v1` (implicite)  
Les futures versions pourraient utiliser `/api/v2/apps` etc.

---

## Support

Pour les issues ou questions, consultez le README principal.


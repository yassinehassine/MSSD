# 🚀 MSSD - Système de Gestion de Formation

Application web complète pour la gestion de formations avec Spring Boot (Backend) + Angular (Frontend).

## 📋 Table des Matières

- [Architecture](#architecture)
- [Démarrage Rapide](#démarrage-rapide)
- [Développement Local](#développement-local)
- [Docker & Production](#docker--production)
- [CI/CD](#cicd)
- [Structure du Projet](#structure-du-projet)

## 🏗️ Architecture

### Stack Technologique
- **Backend**: Spring Boot 3.x + Java 17
- **Frontend**: Angular 18 (Standalone Components)
- **Base de données**: MySQL 8.0
- **Conteneurisation**: Docker + Docker Compose
- **CI/CD**: GitHub Actions
- **Serveur Web**: Nginx (production)

### Services Docker
```
┌─────────────────┐     ┌──────────────────┐     ┌─────────────┐
│   Frontend      │────▶│   Backend API    │────▶│   MySQL     │
│   (Nginx)       │     │  (Spring Boot)   │     │  Database   │
│   Port 80       │     │   Port 8080      │     │  Port 3306  │
└─────────────────┘     └──────────────────┘     └─────────────┘
```

## 🚀 Démarrage Rapide

### Prérequis
- Docker Desktop (Windows/Mac) ou Docker Engine (Linux)
- Git

### Lancer l'application (Mode Production)

```bash
# 1. Cloner le projet
git clone <votre-repo>
cd MSSD-mssd

# 2. Démarrer avec Docker
docker-start.bat          # Windows
# ou
docker compose up -d      # Linux/Mac

# 3. Accéder à l'application
# Frontend:    http://localhost
# Backend:     http://localhost:8080
# phpMyAdmin:  http://localhost:8081 (mode debug)
```

### Scripts Windows disponibles
| Script | Description |
|--------|-------------|
| `docker-start.bat` | Démarrer tous les services |
| `docker-stop.bat` | Arrêter tous les services |
| `docker-logs.bat` | Voir les logs en temps réel |
| `docker-build.bat` | Rebuild les images Docker |
| `docker-clean.bat` | Nettoyer complètement Docker |

## 💻 Développement Local

### Backend (Spring Boot)

```bash
cd mssd-backend

# Avec Maven Wrapper
mvnw spring-boot:run

# Ou avec Maven installé
mvn spring-boot:run

# L'API sera disponible sur http://localhost:8080
```

**Configuration Dev**: 
- Profile: `dev` 
- Base de données: H2 (en mémoire) ou MySQL local
- Fichier: `application-dev.properties`

### Frontend (Angular)

```bash
cd mssd-frontend

# Installer dépendances
npm install

# Démarrer serveur dev
npm start
# ou
ng serve

# L'app sera disponible sur http://localhost:4200
```

**Configuration Dev**: 
- API URL: `http://localhost:8080/api`
- Fichier: `src/environments/environment.ts`

### Base de données locale (sans Docker)

```bash
# MySQL (Port 3306)
mysql -u root -p
CREATE DATABASE mssd_db;

# Ou utiliser phpMyAdmin, MySQL Workbench, etc.
```

## 🐳 Docker & Production

### Configuration Docker

**Structure**:
```
MSSD-mssd/
├── docker-compose.yml        # Orchestration multi-conteneurs
├── .env                      # Variables d'environnement
├── mssd-backend/
│   ├── Dockerfile           # Image backend multi-stage
│   └── .dockerignore
└── mssd-frontend/
    ├── Dockerfile           # Image frontend multi-stage
    ├── nginx.conf           # Configuration Nginx
    └── .dockerignore
```

### Commandes Docker

```bash
# Démarrer
docker compose up -d

# Démarrer avec phpMyAdmin
docker compose --profile debug up -d

# Voir les logs
docker compose logs -f
docker compose logs -f mssd-backend
docker compose logs -f mssd-frontend

# Statut des services
docker compose ps

# Rebuild après changements
docker compose up -d --build

# Arrêter
docker compose down

# Arrêter et supprimer volumes (⚠️ efface la DB)
docker compose down -v

# Nettoyage complet
docker compose down -v --rmi all
docker system prune -f
```

### Variables d'Environnement

Fichier `.env`:
```env
MYSQL_ROOT_PASSWORD=root123
MYSQL_DATABASE=mssd_db
MYSQL_USER=mssd_user
MYSQL_PASSWORD=mssd_pass
```

**⚠️ Pour la production**: Changez ces valeurs par défaut !

### Accès aux conteneurs

```bash
# Shell MySQL
docker compose exec mssd-db mysql -u root -proot123 mssd_db

# Shell Backend
docker compose exec mssd-backend sh

# Shell Frontend
docker compose exec mssd-frontend sh
```

### Health Checks

```bash
# Backend
curl http://localhost:8080/actuator/health

# Frontend
curl http://localhost

# Tous les services
docker compose ps
```

## 🔄 CI/CD

Pipeline GitHub Actions automatisé (`.github/workflows/ci-cd.yml`):

### Workflow
1. **Build & Test** (sur push/PR)
   - Backend: Maven compile + tests
   - Frontend: npm build + lint

2. **Docker Build & Push** (main branch uniquement)
   - Build images multi-stage
   - Push vers Docker Hub
   - Tag: `latest` + SHA commit

3. **Deploy** (main branch uniquement)
   - SSH vers serveur production
   - Pull nouvelles images
   - Redémarrage des services

### Configuration GitHub

**Secrets requis** (Settings → Secrets → Actions):
```
DOCKER_USERNAME      # Nom d'utilisateur Docker Hub
DOCKER_PASSWORD      # Token Docker Hub
SERVER_HOST          # IP du serveur production
SERVER_USER          # Utilisateur SSH
SERVER_SSH_KEY       # Clé privée SSH
```

### Déploiement Production

Sur le serveur:
```bash
# Setup initial
cd /opt
git clone <votre-repo> mssd
cd mssd

# Configuration
cp .env.example .env
nano .env  # Éditer variables

# Démarrer
docker compose up -d

# Mise à jour automatique (via CI/CD)
# → Push sur main déclenche le workflow
```

## 📁 Structure du Projet

```
MSSD-mssd/
├── mssd-backend/                    # API Spring Boot
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/mssd/
│   │   │   │   ├── controller/     # REST Controllers
│   │   │   │   ├── service/        # Business Logic
│   │   │   │   ├── repository/     # JPA Repositories
│   │   │   │   ├── model/          # Entities
│   │   │   │   └── dto/            # Data Transfer Objects
│   │   │   └── resources/
│   │   │       ├── application.properties
│   │   │       ├── application-dev.properties
│   │   │       └── application-prod.properties
│   │   └── test/                   # Tests JUnit
│   ├── pom.xml
│   └── Dockerfile
│
├── mssd-frontend/                   # Application Angular
│   ├── src/
│   │   ├── app/
│   │   │   ├── pages/              # Pages
│   │   │   ├── admin/              # Admin Dashboard
│   │   │   ├── services/           # Services Angular
│   │   │   ├── guards/             # Route Guards
│   │   │   ├── model/              # Interfaces TypeScript
│   │   │   └── shared/             # Composants partagés
│   │   ├── assets/                 # Images, CSS, JS
│   │   └── environments/           # Config dev/prod
│   ├── angular.json
│   ├── package.json
│   ├── Dockerfile
│   └── nginx.conf
│
├── docker-compose.yml              # Orchestration Docker
├── .env                            # Variables env
├── .github/workflows/ci-cd.yml     # Pipeline CI/CD
├── Makefile                        # Commandes Make (Linux)
├── docker-*.bat                    # Scripts Windows
└── README-DEVOPS.md               # Documentation DevOps

```

## 🧪 Tests

### Backend
```bash
cd mssd-backend
mvn test                  # Tous les tests
mvn test -Dtest=MyTest    # Test spécifique
```

### Frontend
```bash
cd mssd-frontend
npm test                  # Tests unitaires
npm run e2e              # Tests end-to-end
```

## 📊 Monitoring

### Logs
```bash
# Docker logs
docker compose logs -f

# Backend logs
docker compose logs -f mssd-backend

# Frontend logs (nginx)
docker compose logs -f mssd-frontend
```

### Métriques
- Backend Actuator: http://localhost:8080/actuator
- Health: http://localhost:8080/actuator/health
- Info: http://localhost:8080/actuator/info

## 🔒 Sécurité

### Bonnes pratiques
- ✅ Changez les mots de passe par défaut dans `.env`
- ✅ Utilisez HTTPS en production (avec Nginx/Traefik)
- ✅ Activez CORS uniquement pour domaines approuvés
- ✅ Sauvegardez régulièrement la base de données
- ✅ Mettez à jour les dépendances régulièrement

### Backup Base de données
```bash
# Backup
docker compose exec mssd-db mysqldump -u root -proot123 mssd_db > backup.sql

# Restore
docker compose exec -T mssd-db mysql -u root -proot123 mssd_db < backup.sql
```

## 🆘 Dépannage

### Le conteneur ne démarre pas
```bash
# Vérifier logs
docker compose logs mssd-backend

# Vérifier santé
docker compose ps
```

### Problème de connexion DB
```bash
# Tester connexion MySQL
docker compose exec mssd-db mysqladmin ping -h localhost -u root -proot123

# Vérifier réseau
docker network inspect mssd-mssd_mssd-network
```

### Port déjà utilisé
```bash
# Windows
netstat -ano | findstr :80
netstat -ano | findstr :8080

# Tuer le processus
taskkill /PID <PID> /F
```

### Frontend ne peut pas joindre le backend
1. Vérifier que backend est up: `docker compose ps`
2. Tester API: `curl http://localhost:8080/actuator/health`
3. Vérifier nginx config: `docker compose exec mssd-frontend cat /etc/nginx/conf.d/default.conf`

## 📚 Documentation

- [README-DEVOPS.md](./README-DEVOPS.md) - Guide DevOps complet
- [CALENDAR_SYSTEM.md](./CALENDAR_SYSTEM.md) - Système de réservation
- [ANNEXES_MODULE_DOCUMENTATION.md](./ANNEXES_MODULE_DOCUMENTATION.md) - Module annexes

## 👥 Contribution

1. Fork le projet
2. Créer une branche (`git checkout -b feature/AmazingFeature`)
3. Commit (`git commit -m 'Add AmazingFeature'`)
4. Push (`git push origin feature/AmazingFeature`)
5. Ouvrir une Pull Request

## 📝 License

Ce projet est sous licence [MIT](LICENSE).

---

**Développé avec ❤️ pour MSSD**

Pour plus d'infos: [README-DEVOPS.md](./README-DEVOPS.md)

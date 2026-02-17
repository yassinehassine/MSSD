.PHONY: help build up down restart logs clean db-shell backend-shell frontend-shell

# Default target
help: ## Show this help
	@echo ============================================
	@echo   MSSD DevOps Commands
	@echo ============================================
	@grep -E '^[a-zA-Z_-]+:.*?## .*$$' $(MAKEFILE_LIST) | sort | awk 'BEGIN {FS = ":.*?## "}; {printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2}'

# ============================================
# Docker Compose Commands
# ============================================
build: ## Build all Docker images
	docker compose build --no-cache

up: ## Start all services
	docker compose up -d

up-debug: ## Start all services with phpMyAdmin
	docker compose --profile debug up -d

down: ## Stop all services
	docker compose down

restart: ## Restart all services
	docker compose down
	docker compose up -d

logs: ## Show logs of all services
	docker compose logs -f

logs-backend: ## Show backend logs
	docker compose logs -f mssd-backend

logs-frontend: ## Show frontend logs
	docker compose logs -f mssd-frontend

logs-db: ## Show database logs
	docker compose logs -f mssd-db

# ============================================
# Status & Health
# ============================================
status: ## Show status of all services
	docker compose ps

health: ## Check health of all services
	@echo "Backend:"
	@curl -s http://localhost:8080/actuator/health | python -m json.tool 2>/dev/null || echo "  Not available"
	@echo "\nFrontend:"
	@curl -s -o /dev/null -w "  HTTP Status: %{http_code}\n" http://localhost:80
	@echo "\nDatabase:"
	@docker compose exec mssd-db mysqladmin ping -h localhost -u root -proot123 2>/dev/null || echo "  Not available"

# ============================================
# Shell Access
# ============================================
db-shell: ## Open MySQL shell
	docker compose exec mssd-db mysql -u root -proot123 mssd_db

backend-shell: ## Open backend container shell
	docker compose exec mssd-backend sh

frontend-shell: ## Open frontend container shell
	docker compose exec mssd-frontend sh

# ============================================
# Cleanup
# ============================================
clean: ## Remove all containers, volumes, and images
	docker compose down -v --rmi all --remove-orphans
	docker system prune -f

clean-volumes: ## Remove only volumes (database data)
	docker compose down -v

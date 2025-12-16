# ==================== MAKEFILE FINAL ====================

# Comando de docker compose
COMPOSE ?= docker compose

# Cargar variables de .env si existe
ifneq (,$(wildcard .env))
  include .env
  export
endif

# Servicios de aplicación
# SERVICES       = discovery-server authorization-server api-gateway enrollment-server notification-server event-store-server frontend-client
SERVICES       = discovery-server authorization-server api-gateway enrollment-server notification-server event-store-server

# Infraestructura
INFRASTRUCTURE = zookeeper postgres-auth postgres-events postgres-enrollment kafka prometheus grafana loki promtail

.DEFAULT_GOAL := help

# ==================== HELP ====================

.PHONY: help
help:
	@echo "========================================="
	@echo "  Proyecto Microservicios + Frontend"
	@echo "========================================="
	@echo "Comandos disponibles:"
	@awk 'BEGIN {FS=":.*##"} /^[a-zA-Z0-9_%-]+:.*##/ { printf "  %-22s %s\n", $$1, $$2 }' $(MAKEFILE_LIST)
	@echo ""
	@echo "Acrónimos disponibles:"
	@echo "  fe    : Frontend (React)"
	@echo "  gw    : API Gateway"
	@echo "  auth  : Auth Server"
	@echo "  es    : Enrollment Server"
	@echo "  disc  : Discovery Server"
	@echo "  notif : Notification Server"
	@echo "  event : Event Store"
	@echo ""

# ==================== GESTIÓN GENERAL ====================

.PHONY: up down restart ps build rebuild dev

#up: ## Levantar TODO (Infra + Servicios)
#	$(COMPOSE) up -d $(INFRASTRUCTURE)
#	$(COMPOSE) up -d $(SERVICES)
#	$(COMPOSE) ps


up: ## Levantar
	$(COMPOSE) up -d $(CORE_INFRA) --wait --wait-timeout 180
	$(COMPOSE) up -d $(OBSERVABILITY)
	$(COMPOSE) up -d $(SERVICES) --wait --wait-timeout 180
	$(COMPOSE) ps

down: ## Detener y eliminar todos los contenedores
	$(COMPOSE) down

restart: ## Reiniciar todos los servicios
	$(COMPOSE) restart

ps: ## Ver estado de contenedores
	$(COMPOSE) ps

build: ## Construir imágenes (con caché)
	$(COMPOSE) build

rebuild: ## Reconstruir todo sin caché
	$(COMPOSE) build --no-cache

dev: clean build up ## Workflow completo (Clean -> Build -> Up)


# ==================== REBUILD INDIVIDUAL (Construir y Levantar uno solo) ====================
# Útil cuando cambias código en un solo servicio y quieres actualizarlo

.PHONY: rebuild-fe rebuild-gw rebuild-auth rebuild-es rebuild-disc rebuild-cfg rebuild-notif rebuild-event

rebuild-fe: ## Reconstruir Frontend
	$(COMPOSE) up -d --build --no-deps frontend-client

rebuild-gw: ## Reconstruir Gateway
	$(COMPOSE) up -d --build --no-deps api-gateway

rebuild-auth: ## Reconstruir Auth Server
	$(COMPOSE) up -d --build --no-deps authorization-server

rebuild-es: ## Reconstruir Enrollment
	$(COMPOSE) up -d --build --no-deps enrollment-server

rebuild-disc: ## Reconstruir Discovery
	$(COMPOSE) up -d --build --no-deps discovery-server

rebuild-notif: ## Reconstruir Notification
	$(COMPOSE) up -d --build --no-deps notification-server

rebuild-event: ## Reconstruir Event Store
	$(COMPOSE) up -d --build --no-deps event-store-server


# ==================== RESTART INDIVIDUAL (Reiniciar sin reconstruir) ====================
# Útil si el servicio se colgó o cambiaste una variable de entorno

.PHONY: restart-fe restart-gw restart-auth restart-es restart-disc restart-cfg restart-notif restart-event

restart-fe: ## Reiniciar Frontend
	$(COMPOSE) restart frontend-client

restart-gw: ## Reiniciar Gateway
	$(COMPOSE) restart api-gateway

restart-auth: ## Reiniciar Auth Server
	$(COMPOSE) restart authorization-server

restart-es: ## Reiniciar Enrollment
	$(COMPOSE) restart enrollment-server

restart-disc: ## Reiniciar Discovery
	$(COMPOSE) restart discovery-server

restart-notif: ## Reiniciar Notification
	$(COMPOSE) restart notification-server

restart-event: ## Reiniciar Event Store
	$(COMPOSE) restart event-store-server


# ==================== UP INDIVIDUAL (Levantar si está apagado) ====================

.PHONY: up-fe up-gw up-auth up-es up-disc up-cfg up-notif up-event

up-fe: ## Levantar solo Frontend
	$(COMPOSE) up -d frontend-client

up-gw: ## Levantar solo Gateway
	$(COMPOSE) up -d api-gateway

up-auth: ## Levantar solo Auth Server
	$(COMPOSE) up -d authorization-server

up-es: ## Levantar solo Enrollment
	$(COMPOSE) up -d enrollment-server

up-disc: ## Levantar solo Discovery
	$(COMPOSE) up -d discovery-server

up-notif: ## Levantar solo Notification
	$(COMPOSE) up -d notification-server

up-event: ## Levantar solo Event Store
	$(COMPOSE) up -d event-store-server


# ==================== LOGS INDIVIDUALES ====================

.PHONY: logs logs-fe logs-gw logs-auth logs-es logs-disc logs-cfg logs-notif logs-event

logs: ## Ver todos los logs
	$(COMPOSE) logs -f

logs-fe: ## Logs Frontend
	$(COMPOSE) logs -f frontend-client

logs-gw: ## Logs Gateway
	$(COMPOSE) logs -f api-gateway

logs-auth: ## Logs Auth Server
	$(COMPOSE) logs -f authorization-server

logs-es: ## Logs Enrollment
	$(COMPOSE) logs -f enrollment-server

logs-disc: ## Logs Discovery
	$(COMPOSE) logs -f discovery-server

logs-notif: ## Logs Notification
	$(COMPOSE) logs -f notification-server

logs-event: ## Logs Event Store
	$(COMPOSE) logs -f event-store-server


# ==================== INFRAESTRUCTURA Y MONITOREO ====================

.PHONY: infra-up infra-down monitoring-up monitoring-down

#infra-up: ## Levantar solo BDs, Kafka, Zookeeper
#	$(COMPOSE) up -d $(INFRASTRUCTURE)

infra-up:
	$(COMPOSE) up -d $(CORE_INFRA) --wait --wait-timeout 180

infra-down: ## Detener infraestructura
	$(COMPOSE) stop $(INFRASTRUCTURE)

monitoring-up: ## Levantar Prometheus/Grafana
	$(COMPOSE) up -d prometheus grafana

monitoring-down: ## Apagar Prometheus/Grafana
	$(COMPOSE) stop prometheus grafana


# ==================== LIMPIEZA ====================

.PHONY: clean clean-all

clean: ## Detener y eliminar contenedores
	$(COMPOSE) down

clean-all: ## Detener y eliminar todo (incluido volúmenes/datos)
	$(COMPOSE) down -v

# ==================== UTILIDADES ====================

.PHONY: env
env: ## Mostrar variables de entorno (.env)
	@echo "========================================="
	@echo "  Variables de Entorno"
	@echo "========================================="
	@cat .env 2>/dev/null || echo "❌ Archivo .env no encontrado"

.PHONY: urls
urls: ## Mostrar URLs de los servicios
	@echo "========================================="
	@echo "  URLs de Servicios"
	@echo "========================================="
	@echo ""
	@echo "Discovery Server (Eureka):"
	@echo "  http://localhost:$(EUREKA_PORT)"
	@echo ""
	@echo "API Gateway:"
	@echo "  http://localhost:$(API_GATEWAY_PORT)"
	@echo ""
	@echo "Swagger UI Gateway:"
	@echo "  http://localhost:$(API_GATEWAY_PORT)/webjars/swagger-ui/index.html"
	@echo ""
	@echo "Grafana Dashboard:"
	@echo "  http://localhost:$(GRAFANA_PORT)"
	@echo ""
	@echo "Prometheus:"
	@echo "  http://localhost:$(PROMETHEUS_PORT)"
	@echo ""
	@echo "Auth:"
	@echo "  http://localhost:$(API_GATEWAY_PORT)/auth/..."
	@echo "Enrollment:"
	@echo "  http://localhost:$(API_GATEWAY_PORT)/api/v1/enrollment..."
	@echo ""

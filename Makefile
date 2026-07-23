# ============================================
#   Comandos del proyecto  ->  make <comando>
# ============================================

COMPOSE = docker compose

GIT_COMMIT      := $(shell git rev-parse HEAD 2>/dev/null || echo unknown)
GIT_BRANCH      := $(shell git branch --show-current 2>/dev/null || echo unknown)
GIT_COMMIT_TIME := $(shell git log -1 --format=%cI 2>/dev/null || echo unknown)
export GIT_COMMIT GIT_BRANCH GIT_COMMIT_TIME

# Carga las variables del archivo .env
ifneq (,$(wildcard .env))
  include .env
  export
endif

.DEFAULT_GOAL := help

help:
	@echo ""
	@echo "  make up      -> Construir y levantar todo (uso diario)"
	@echo "  make down    -> Apagar todo (NO borra datos)"
	@echo "  make logs    -> Ver los logs en vivo (Ctrl+C para salir)"
	@echo "  make ps      -> Ver qué está corriendo"
	@echo "  make reset   -> Borrar TODO incluido bases de datos y levantar de cero"
	@echo "  make prod    -> Levantar en el servidor (sin puertos de debug)"
	@echo "  make sonar-up   -> Levantar SonarQube (http://localhost:9000)"
	@echo "  make sonar      -> Correr tests + analisis SonarQube (SONAR_TOKEN en .env)"
	@echo "  make sonar-down -> Apagar SonarQube (los datos persisten)"
	@echo ""

up:
	$(COMPOSE) up -d --build --wait --wait-timeout 180
	$(COMPOSE) ps

down:
	$(COMPOSE) down

logs:
	$(COMPOSE) logs -f

ps:
	$(COMPOSE) ps

reset:
	$(COMPOSE) down -v
	$(COMPOSE) up -d --build --wait --wait-timeout 180
	$(COMPOSE) ps

prod:
	docker compose -f docker-compose.yml pull
	docker compose -f docker-compose.yml up -d --wait --wait-timeout 180
	docker compose -f docker-compose.yml ps

sonar-up:
	docker compose -f docker-compose.sonarqube.yml up -d --wait --wait-timeout 300

sonar-down:
	docker compose -f docker-compose.sonarqube.yml down

# Tests con cobertura (solo enrollment-server tiene tests hoy) + análisis.
# Requiere: make sonar-up y SONAR_TOKEN en .env (User > My Account > Security).
sonar:
	mvn clean verify sonar:sonar \
		-Dsonar.host.url=$(if $(SONAR_HOST_URL),$(SONAR_HOST_URL),http://localhost:9000) \
		-Dsonar.token=$(SONAR_TOKEN)

.PHONY: help up down logs ps reset prod sonar-up sonar-down sonar

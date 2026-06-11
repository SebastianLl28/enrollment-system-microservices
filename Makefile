# ============================================
#   Comandos del proyecto  ->  make <comando>
# ============================================

COMPOSE = docker compose

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
	docker compose -f docker-compose.yml up -d --build --wait --wait-timeout 180
	docker compose -f docker-compose.yml ps

.PHONY: help up down logs ps reset prod

# ============================================================================
# INFRAESTRUCTURA COMO CÓDIGO (IaC) - TERRAFORM PARA RAILWAY CLOUD
# Aprovisiona el proyecto admin-proyectos, Base de Datos MySQL,
# MicroservicioBack y MicroservicioFront en Railway conectados a GitHub.
# ============================================================================
terraform {
  required_version = ">= 1.5.0"
  required_providers {
    railway = {
      source  = "terraform-community-providers/railway"
      version = "~> 0.4.0"
    }
  }
}

variable "railway_token" {
  description = "Token de API de Railway (inyectado por variable de entorno RAILWAY_TOKEN, no expuesto en Git)"
  type        = string
  sensitive   = true
}

variable "github_repo" {
  description = "Repositorio de GitHub conectado a Railway"
  type        = string
  default     = "Jen031205/admin-proyectos"
}

provider "railway" {
  token = var.railway_token
}

resource "railway_project" "admin_proyectos" {
  name        = "admin-proyectos-devops"
  description = "Circuito DevOps Cloud: Frontend, Backend API y Base de Datos MySQL"
}

resource "railway_service" "mysql_db" {
  name       = "mariadb-dbgestor"
  project_id = railway_project.admin_proyectos.id
  source_image = "mysql:8.0"
}

resource "railway_service" "microservicio_back" {
  name            = "MicroservicioBack"
  project_id      = railway_project.admin_proyectos.id
  source_repo     = var.github_repo
  root_directory  = "/"
}

resource "railway_service" "microservicio_front" {
  name            = "MicroservicioFront"
  project_id      = railway_project.admin_proyectos.id
  source_repo     = var.github_repo
  root_directory  = "/"
}

output "railway_project_id" {
  description = "ID del proyecto aprovisionado en Railway Cloud"
  value       = railway_project.admin_proyectos.id
}

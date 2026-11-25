variable "aws_region" {
  description = "Región de AWS donde se aprovisionará la infraestructura del proyecto admin-proyectos"
  type        = string
  default     = "us-east-1"
}

variable "project_name" {
  description = "Nombre identificador del proyecto integrador"
  type        = string
  default     = "admin-proyectos-devops"
}

variable "environment" {
  description = "Entorno de despliegue (dev, staging, prod)"
  type        = string
  default     = "prod"
}

variable "instance_type" {
  description = "Tamaño de instancia EC2 para alojar los microservicios y monitoreo (Free Tier / t3.medium)"
  type        = string
  default     = "t3.small"
}

variable "ssh_public_key" {
  description = "Llave pública SSH para acceso de Ansible (NO incluir llaves privadas en el repositorio)"
  type        = string
  sensitive   = true
}

variable "allowed_cidr" {
  description = "Rango CIDR autorizado para acceder a los servicios web y tableros de monitoreo"
  type        = string
  default     = "0.0.0.0/0"
}

variable "db_password" {
  description = "Contraseña del motor MariaDB inyectada como variable sensible en tiempo de ejecución"
  type        = string
  sensitive   = true
}

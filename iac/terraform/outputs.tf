output "server_public_ip" {
  description = "Dirección IP pública de la instancia Cloud aprovisionada"
  value       = aws_instance.app_server.public_ip
}

output "frontend_url" {
  description = "URL de acceso al microservicio Frontend (Ejercicio5Cliente)"
  value       = "http://${aws_instance.app_server.public_ip}:8080"
}

output "backend_api_url" {
  description = "URL base de la API REST Backend (Ejercicio5MyAPIGestor)"
  value       = "http://${aws_instance.app_server.public_ip}:8081/usuarios"
}

output "jenkins_cloud_url" {
  description = "URL del servidor Jenkins CI/CD desplegado en la nube"
  value       = "http://${aws_instance.app_server.public_ip}:8082"
}

output "mariadb_connection" {
  description = "Host y puerto de la Base de Datos MariaDB (dbgestor) en la nube"
  value       = "${aws_instance.app_server.public_ip}:3307 (Esquema: dbgestor)"
}

output "grafana_dashboard_url" {
  description = "URL del tablero de monitoreo y observabilidad en Grafana"
  value       = "http://${aws_instance.app_server.public_ip}:3000"
}

output "prometheus_metrics_url" {
  description = "URL del servidor de métricas y alertas en Prometheus"
  value       = "http://${aws_instance.app_server.public_ip}:9090"
}

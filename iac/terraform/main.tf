# ============================================================================
# INFRAESTRUCTURA COMO CÓDIGO (IaC) - APROVISIONAMIENTO CLOUD (TERRAFORM)
# Proyecto: admin-proyectos (Frontend, Backend, MariaDB, Jenkins y Monitoreo)
# ============================================================================

data "aws_ami" "ubuntu_2204" {
  most_recent = true
  owners      = ["099720109477"] # Canonical

  filter {
    name   = "name"
    values = ["ubuntu/images/hvm-ssd/ubuntu-jammy-22.04-amd64-server-*"]
  }

  filter {
    name   = "virtualization-type"
    values = ["hvm"]
  }
}

resource "aws_key_pair" "deployer_key" {
  key_name   = "${var.project_name}-key"
  public_key = var.ssh_public_key
}

resource "aws_security_group" "devops_sg" {
  name        = "${var.project_name}-sg"
  description = "Reglas de red para Frontend (8080), Backend (8081), Jenkins (8082), MariaDB (3307), Prometheus (9090) y Grafana (3000)"

  # Acceso SSH para aprovisionamiento con Ansible
  ingress {
    description = "SSH para gestion de configuracion con Ansible"
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Microservicio Frontend (Ejercicio5Cliente)
  ingress {
    description = "HTTP Microservicio Frontend Ejercicio5Cliente"
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Microservicio Backend (Ejercicio5MyAPIGestor)
  ingress {
    description = "HTTP Microservicio Backend Ejercicio5MyAPIGestor"
    from_port   = 8081
    to_port     = 8081
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Servidor CI/CD Jenkins en la Nube
  ingress {
    description = "HTTP Servidor Jenkins CI/CD"
    from_port   = 8082
    to_port     = 8082
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Base de Datos MariaDB (dbgestor)
  ingress {
    description = "TCP Base de Datos MariaDB dbgestor"
    from_port   = 3307
    to_port     = 3307
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Tablero de Observabilidad Grafana
  ingress {
    description = "HTTP Tablero Grafana"
    from_port   = 3000
    to_port     = 3000
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  # Servidor de Métricas Prometheus
  ingress {
    description = "HTTP Metricas Prometheus"
    from_port   = 9090
    to_port     = 9090
    protocol    = "tcp"
    cidr_blocks = [var.allowed_cidr]
  }

  egress {
    description = "Salida permitida para descarga de paquetes e imagenes Docker"
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_instance" "app_server" {
  ami                    = data.aws_ami.ubuntu_2204.id
  instance_type          = var.instance_type
  key_name               = aws_key_pair.deployer_key.key_name
  vpc_security_group_ids = [aws_security_group.devops_sg.id]

  root_block_device {
    volume_size           = 25
    volume_type           = "gp3"
    encrypted             = true
    delete_on_termination = true
  }

  user_data = <<-EOF
              #!/bin/bash
              set -e
              apt-get update -y
              apt-get install -y ca-certificates curl gnupg git docker.io docker-compose-v2
              systemctl enable docker
              systemctl start docker
              usermod -aG docker ubuntu
              EOF

  tags = {
    Name = "${var.project_name}-server"
    Role = "FullStack-Jenkins-DB-Microservices-Monitoring"
  }
}

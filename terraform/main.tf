
// main.tf
provider "aws" {
  region = "eu-west-3" # Paris region
}

module "vpc" {
  source = "./vpc.tf"
}

module "security" {
  source = "./security_groups.tf"
}

module "instances" {
  source = "./instances.tf"
}

module "s3" {
  source = "./s3.tf"
}

output "database_server_ip" {
  value = module.instances.database_server_ip
}

// vpc.tf
resource "aws_vpc" "micromall_vpc" {
  cidr_block = "10.0.0.0/16"
}

resource "aws_subnet" "database_subnet" {
  vpc_id     = aws_vpc.micromall_vpc.id
  cidr_block = "10.0.1.0/24"
  availability_zone = "eu-west-3a"
}

resource "aws_subnet" "devops_subnet" {
  vpc_id     = aws_vpc.micromall_vpc.id
  cidr_block = "10.0.2.0/24"
  availability_zone = "eu-west-3a"
}

resource "aws_subnet" "k8s_subnet" {
  vpc_id     = aws_vpc.micromall_vpc.id
  cidr_block = "10.0.3.0/24"
  availability_zone = "eu-west-3a"
}

resource "aws_subnet" "docker_subnet" {
  vpc_id     = aws_vpc.micromall_vpc.id
  cidr_block = "10.0.4.0/24"
  availability_zone = "eu-west-3a"
}

// security_groups.tf
resource "aws_security_group" "ssh_sg" {
  vpc_id = aws_vpc.micromall_vpc.id

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

// instances.tf
resource "aws_instance" "database_server" {
  ami           = "ami-12345678" # Example AMI, replace with real one
  instance_type = "t3.medium"
  subnet_id     = aws_subnet.database_subnet.id
  security_groups = [aws_security_group.ssh_sg.name]
}

// s3.tf
resource "aws_s3_bucket" "micromall_bucket" {
  bucket = "micromall-product-images"
  acl    = "private"
}

// outputs.tf
output "database_server_ip" {
  value = aws_instance.database_server.public_ip
}

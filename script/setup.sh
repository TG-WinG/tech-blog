#!/bin/bash
set -eux

# Docker 설치 및 실행
dnf install -y docker
systemctl enable --now docker
usermod -aG docker ec2-user

# Docker Compose 설치
curl -L "https://github.com/docker/compose/releases/latest/download/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
chmod +x /usr/local/bin/docker-compose
ln -s /usr/local/bin/docker-compose /usr/bin/docker-compose

# redis 설치
sudo dnf install redis6 -y  # Redis6 설치

# AWS CodeDeploy Agent 설치
dnf install -y ruby wget
cd /home/ec2-user
wget https://aws-codedeploy-us-east-1.s3.us-east-1.amazonaws.com/latest/install
chmod +x ./install
./install auto
systemctl enable --now codedeploy-agent

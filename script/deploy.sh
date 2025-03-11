#!/bin/bash
set -eux

# Docker 로그인
docker login -u ${DOCKER_USERNAME} -p ${DOCKER_PASSWORD}

# Docker 이미지 pull
docker pull ${DOCKER_USERNAME}/docker-test:latest

# 기존 컨테이너 중지 및 삭제
if [ "$(docker ps -q -f name=spring-app)" ]; then
  docker stop spring-app
  docker rm spring-app
fi

# docker-compose 실행
cd /home/ec2-user/app
docker-compose up -d --no-deps app

# 불필요한 이미지 삭제
docker image prune -a -f

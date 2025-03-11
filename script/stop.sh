#!/bin/bash
set -eux

# spring-app 컨테이너가 실행 중이면 중지 및 삭제
if [ "$(docker ps -q -f name=spring-app)" ]; then
  docker stop spring-app
  docker rm spring-app
fi

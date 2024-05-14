#!/bin/bash

# 기존 프로세스를 shutdown 후 새 jar를 배포하기 전 기존 jar를 백업하는 용도로 쓰인다.
# server.jar 파일이 있다면 server.bak.jar로 백업하고, 아니면 아무것도 하지 않는다.

if [ -f server.jar ]; then
  sudo mv server.jar server.bak.jar
  echo "server.bak.jar created."
  exit 0
fi

echo "no server.jar file found."
exit 0

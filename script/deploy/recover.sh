#!/bin/bash

# 새로운 jar 배포가 실패했을 때 백업된 기존 jar 로 다시 서버를 시작한다.

script_dir=$(dirname "$0")

if [ -f "server.bak.jar" ]; then
  sudo mv server.bak.jar server.jar
  echo "backup jar recovered."
  "${script_dir}/runner.sh" server.jar
  exit_code=$?

  if [ $exit_code -ne 0 ]; then
    echo "recovory fail: error occured starting backup jar."
    exit 1
  fi

  exit 0
fi

echo "recovory fail: no backup jar file found"
exit 2

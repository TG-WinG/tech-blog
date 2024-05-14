#!/bin/bash

# 서버의 상태를 확인하여 실행시 실패했다면 복구한다.

pid=$(sudo lsof -t -i :8080)

if [ -z "$pid" ]; then
  echo "Error: server is not running."

  echo 
  echo "========= ERROR LOG =========="
  cat out.log
  echo "========== LOG END ==========="
  echo 

  script_dir=$(dirname "$0")

  if [ -f "server.bak.jar" ]; then
    sudo mv server.bak.jar server.jar
    echo "backup jar recovered."

    "${script_dir}/startup.sh"
    exit 1
  fi

  echo "recovory fail: no backup jar file found"
  exit 2

fi

echo "PID '${pid}' is running on port 8080."


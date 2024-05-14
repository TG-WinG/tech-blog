#!/bin/bash

# 서버가 실행중인지 확인한다.

pid=$(sudo lsof -t -i :8080)

if [ -z "$pid" ]; then
  echo "Error: server is not running."
  exit 1
fi

echo "PID '${pid}' is running on port 8080."

#!/bin/bash

# 기존 server 프로세스를 끝낸다. 

pid=$(sudo lsof -t -i :8080)
if [ -n "$pid" ]; then
  kill $pid
  echo "${pid} closed."
  exit 0
fi

echo "no process running on port 8080."

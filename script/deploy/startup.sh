#!/bin/bash

# server.jar 로 새로운 서버 프로세스를 실행한다.

pid=$(sudo lsof -t -i :8080)

if [ -n "$pid" ]; then
  echo "Error: PID '${pid}' is conneted to port 8080. cannot startup new process."
  exit 1
fi
nohup java -jar server.jar --spring.profiles.active=dev > out.log 2>&1 & disown
echo "server started at port 8080."
echo "stdout & stderr directed to 'out.log'"

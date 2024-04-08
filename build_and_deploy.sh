#!/bin/bash

# 환경변수 설정
source env.sh

host=$EC2_HOST
username=$EC2_USERNAME

output=$(./gradlew bootJar --info)
status=$?

if [ $status -ne 0 ]; then
  echo " === build error ==="
  echo "$output"
  exit 1
fi

# Upload deployment scripts
chmod 755 script/deploy/*
scp -i private_key.pem -r script/deploy/* "${username}@${host}:deploy"

# Kill current running Spring process
ssh -i private_key.pem "${username}@${host}" "deploy/shutdown.sh; deploy/backup.sh"

# Send jar file to EC2
jarPath=$(./gradlew -q jarPath)
scp -i private_key.pem $jarPath "${username}@${host}:~/server.jar"

# Startup new uploaded jar
ssh -i private_key.pem "${username}@${host}" "source ~/.env; deploy/startup.sh"

# Wait 30 seconds for server to startup
echo "wating for 30 seconds..."
sleep 30

# Check and Recover
ssh -i private_key.pem "${username}@${host}" "source ~/.env; deploy/check_and_recover.sh"
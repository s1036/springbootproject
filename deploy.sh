#!/bin/bash

REPOSITORY=/home/ec2-user/app/project
PROJECT_NAME=springbootproject

cd $REPOSITORY

echo " 현재 구동중인 어플리케이션 pid 확인"

CURRENT_PID=$(pgrep -f ${PROJECT_NAME}*.jar)

echo "현재 구동중인 어플리케이션 pid : $CURRENT_PID"

if [ -z "$CURRENT_PID" ]; then
echo ">현재 구동중인 어플리케이션이 없으므로 종료하지 않습니다."
    else
        echo "> kill -15 $CURRENT_PID"
        kill -15 $CURRENT_PID
    sleep 5
fi

echo "> 새 어플리케이션 배포"

JAR_NAME=$(ls -tr $REPOSITORY/ | grep *.jar | tail -n 1)

echo "> JAR NAME: $JAR_NAME"

nohup java -jar $REPOSITORY/$JAR_NAME 2>&1 &


#!/bin/bash
export KAKAO_CLIENT_ID=4f6a4fd0bafc7a5f5f4809b035e5884c
export KAKAO_CLIENT_SECRET=apWAUTOudBGQqJDfHIhODdqWVkpcNvUu
export KAKAO_HTTP_HOST=http://localhost:8080
export DB_HOST=127.0.0.1
export DB_PORT=3375
export DB_NAME=mimi
export DB_USER=root
export DB_PASS=1123

./gradlew -Dspring.profiles.active=prod
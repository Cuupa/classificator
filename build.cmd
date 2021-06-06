@echo off
mvn clean verify -U
echo "[INFO] Copying jars"
copy app\build\*.jar build
echo "[INFO] BUILD SUCCESS"
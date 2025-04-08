#!/bin/bash

# Crear una imagen que ejecute el script "clima.sh"

cd ./clima

docker build -t irueda/clima-job:1 .

# Desplegar el cronjob

kubectl apply -f clima-cronjob.yaml

echo ""
echo "Clima cronjob desplegado"
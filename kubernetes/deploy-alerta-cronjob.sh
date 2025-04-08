#!/bin/bash

# Crear una imagen que ejecute el script "alerta.sh"

cd ./alerta

docker build -t irueda/alerta-job:1 .

# Desplegar el cronjob

kubectl apply -f alerta-cronjob.yaml

echo ""
echo "Alerta cronjob desplegado"
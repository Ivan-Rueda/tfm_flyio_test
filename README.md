# TFM API Rest
## Uso de dispositivos LoRa para monitorizar equipos de protección individual (EPIs) por medio de una infraestructura de computación en la nube

## Crear imagen docker
Ejecutar desde el directorio raíz

```
docker build -t irueda/tfm-api:1 .
```

## Desplegar cronjobs de clima y alerta en K8s

Para desplegar los ```cronjobs```  de clima y alerta, primero se debe reemplazar en los scripts ```clima.sh``` y ```alerta.sh``` (ubicados en la carpeta kubernetes) la dirección del servidor por el nombre del servicio que lo expone en kubernetes.

Por ejemplo:
```
http://localhost:8080/api/registros/v1/lastHour
```
por
```
http://api-service/api/registros/v1/lastHour
```
__Nota:__ Se debe asumir que todo se encuentra dentro del mismo nasmespace

Luego se puede ejecutar el script de despliegue correspondiente desde el directorio ```/kubernetes```
```
./deploy-clima-cronjob.sh
```

```
./deploy-alerta-cronjob.sh
```
# Datos de prueba semilla
La base de en ejecución tendrá datos. Los llamados a API ```seed``` borraran los datos del Mongo DB de los documentos (registros) de las colecciones (tablas). Solo se recomienda utilizarlos al cargar una base de datos en blanco.

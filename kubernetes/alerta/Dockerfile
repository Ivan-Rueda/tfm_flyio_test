FROM alpine

RUN apk add --no-cache curl jq bash

# Copia el script dentro de la imagen
COPY alerta.sh /usr/local/bin/alerta.sh

# Asegúrate de que el script tenga permisos de ejecución
RUN chmod +x /usr/local/bin/alerta.sh

# Establece el punto de entrada para que ejecute el script por defecto
ENTRYPOINT ["/usr/local/bin/alerta.sh"]

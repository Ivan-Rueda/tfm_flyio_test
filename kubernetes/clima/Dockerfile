FROM alpine

RUN apk add --no-cache curl jq bash

# Copia el script dentro de la imagen
COPY clima.sh /usr/local/bin/clima.sh

# Asegúrate de que el script tenga permisos de ejecución
RUN chmod +x /usr/local/bin/clima.sh

# Establece el punto de entrada para que ejecute el script por defecto
ENTRYPOINT ["/usr/local/bin/clima.sh"]

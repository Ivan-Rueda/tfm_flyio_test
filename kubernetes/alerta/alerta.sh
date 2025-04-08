#!/bin/bash

crear_alerta_body() {
    local tipo="$1"
    local descripcion="$2"
    local latitud="$3"
    local longitud="$4"
    local macDispositivo="$5"

    # Genera el objeto JSON de la alerta
    local alerta_body=$(jq -n \
        --arg tipo "$tipo" \
        --arg descripcion "$descripcion" \
        --arg latitud "$latitud" \
        --arg longitud "$longitud" \
        --arg macDispositivo "$macDispositivo" \
        '{tipo: $tipo | tostring, 
        descripcion: $descripcion | tostring, 
        latitud: $latitud | tonumber, 
        longitud: $longitud | tonumber, 
        macDispositivo: $macDispositivo | tostring}')

    # Devuelve la variable alerta_body a consola
    echo "$alerta_body"
    echo ""
}

# Obtener la última alerta creada para evitar duplicados de alertas
last_alert=$(curl --location 'http://192.168.1.133:8080/api/alertas/v1' | jq -c 'max_by(.createdAt)')

last_alert_date=$(echo "$last_alert" | jq -r '.createdAt')
echo "Última alerta creada en: $last_alert_date"

# Listar todos los registros de la última hora
registers=$(curl --location 'http://192.168.1.133:8080/api/registros/v1/lastHour')

sleep .5

echo "$registers"

if [ "$(echo "$registers" | jq '. | length')" -eq 0 ]; then
    echo "No hay registros que listar."
    exit 1
fi

alertas_url="http://192.168.1.133:8080/api/alertas/v1"

# Recorrer cada registro
echo "$registers" | jq -c '.[]' | while read -r registro; do

    registro_created_at=$(echo "$registro" | jq -r '.createdAt')

    # Comparar la fecha del registro con la fecha de la última alerta, 
    # Si la fecha de creación del registro es posterior a la fecha de la última alerta ono existe una última alerta,
    # entonces el registro es más reciente y debería ser procesado.
    if [[ -z "$last_alert_date" || "$registro_created_at" > "$last_alert_date" ]]; then
        res=$(echo "$registro" | jq '.resultados')
        lat=$(echo "$registro" | jq '.latitud')
        lng=$(echo "$registro" | jq '.longitud')
        macD=$(echo "$registro" | jq -r '.macDispositivo')

        uso=$(echo "$res" | jq '.uso')
        movimiento=$(echo "$res" | jq '.movimiento')
        temperatura=$(echo "$res" | jq '.temperatura')

    # Luego de extraer la data del registro, se procede a comparar los valores de uso, movimiento y temperatura
    # IMPORTANTE: estos datos han sido registrados dede LoRa con los datos provenientes de un trabajador
    # En caso de que alguno de estos valores sea falso o esté fuera de los rangos permitidos, se crea una alerta con la API de alertas
        if [ "$uso" = false ]; then
            echo ""
            echo "Crear alerta de uso"
            alerta_body=$(crear_alerta_body "Alerta de uso" "No se está usando el EPI." "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        if [ "$movimiento" = false ]; then
            echo ""
            echo "Crear alerta de movimiento"
            alerta_body=$(crear_alerta_body "Alerta de movimiento" "No se reporta movimiento del trabajador." "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        if [[ "$temperatura" -lt 10 || "$temperatura" -gt 27 ]]; then
            echo ""
            echo "Crear alerta de temperatura reportada por el trabajador (LoRa)"
            alerta_body=$(crear_alerta_body "Alerta de T° en trabajador" "La temperatura reportada desde el trabajador es ${last_temperatura} (menor de 10 o mayor a 27). " "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        # Obtener el último clima según latitud y longitud de la API climas (cuya información proviene de una API externa de climatología)
        last_clima=$(curl --location "http://192.168.1.133:8080/api/climas/v1/last?latitud=${lat}&longitud=${lng}")

        sleep .5

        if [ "$last_clima" = "No clima found" ]; then
            echo ""
            echo "No se encontró clima para latitud $lat y longitud $lng"
            continue
        fi

        last_temperatura=$(echo "$last_clima" | jq '.temperatura')
        last_humedad=$(echo "$last_clima" | jq '.humedad')
        last_viento=$(echo "$last_clima" | jq '.viento')
        last_uv=$(echo "$last_clima" | jq '.uv')

        # Luego de extraer la data del registro, se procede a comparar los valores de temperatura, humedad, viento y uv
        # IMPORTANTE: estos datos han sido registrados desde un proveedor externo de API de climatologia
        # En caso de que alguno de estos valores esté fuera de los rangos permitidos, se crea una alerta con la API de alertas

        # Comparar valores de temperatura, humedad, viento y uv
        if [[ $last_temperatura -lt 10 || $last_temperatura -gt 27 ]]; then
            echo ""
            echo "Crear alerta de temperatura reportada por la API externa"
            alerta_body=$(crear_alerta_body "Alerta de T° Clima" "La temperatura reportada por clima es ${last_temperatura} (menor de 10 o mayor a 27). Se advierten condiciones extremas. " "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        if [ "$last_humedad" -gt 70 ]; then
            echo ""
            echo "Crear alerta de humedad"
            alerta_body=$(crear_alerta_body "Alerta de humedad" "La humedad es ${last_humedad} (mayor a 70%). Se advierten condiciones extremas. " "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        if [ "$last_viento" -gt 15 ]; then
            echo ""
            echo "Crear alerta de viento"
            alerta_body=$(crear_alerta_body "Alerta de viento" "El viento es ${last_viento} (mayor a 15 Km/h). Se advierten condiciones extremas. " "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        if [ "$last_uv" -gt 5 ]; then
            echo ""
            echo "Crear alerta de uv"
            alerta_body=$(crear_alerta_body "Alerta de uv" "El uv es ${last_uv} (mayor a 5). Se advierten condiciones extremas." "$lat" "$lng" "$macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .3
        fi

        # Luego consulta los cursos por medio de la API de empleados y la API de Moodle externa
        # Recorrer todos los empleados y verificar el estado de sus cursos en Moodle
        empleados=$(curl --location 'http://192.168.1.133:8080/api/empleados/v1')

        echo "$empleados" | jq -c '.[]' | while read -r empleado; do
            empleado_id=$(echo "$empleado" | jq -r '.id')
            moodle_userid=$(echo "$empleado" | jq -r '.id_moodle')
            nombre_empleado=$(echo "$empleado" | jq -r '.nombres')
            apellido_empleado=$(echo "$empleado" | jq -r '.apellidos')
            telefono_empleado=$(echo "$empleado" | jq -r '.n_telefono')

            cursos=$(curl --location -k "https://localhost/webservice/rest/server.php" \
                    --header 'Content-Type: application/x-www-form-urlencoded' \
                    --data-urlencode "wstoken=d73ee4b0d9a0f59bbd9ed0eccda7d6c9" \
                    --data-urlencode 'wsfunction=core_enrol_get_users_courses' \
                    --data-urlencode 'moodlewsrestformat=json' \
                    --data-urlencode "userid=${moodle_userid}")

            echo "Cursos de $nombre_empleado: $cursos"

            # Revisar si algún curso no está completado para generar una alerta
            echo "$cursos" | jq -c '.[]' | while read -r curso; do
                completed=$(echo "$curso" | jq -r '.completed')

                if [ "$completed" = false ]; then
                    echo ""
                    echo "Crear alerta de curso no completado para $nombre_empleado"
                    alerta_body=$(crear_alerta_body "Alerta por curso" "El curso de EPIs no ha sido completado por $nombre_empleado $apellido_empleado ." "$lat" "$lng" "$macD")

                    curl --location "$alertas_url" \
                    --header 'Content-Type: application/json' \
                    --data "$alerta_body"
                    sleep .3

                    # Crear el mensaje con variables
                    mensaje="🚨 Hola $nombre_empleado. Debes completar el curso EPIs101, apunta a ser el mejor, contamos contigo.😀 👷‍♂️"

                    curl --location --request POST "https://api.smsapi.com/sms.do" \
                    --header 'Authorization: Bearer rcpu90gFopVNEgBKDVQs8evivQawjj4rSTBE6cXs' \
                    --data-urlencode "to=${telefono_empleado}" \
                    --data-urlencode "message=${mensaje}" \
                    --data-urlencode "format=json"
                    sleep .3
                fi
            done
        done

    else
        echo "Registro creado en $registro_created_at fue ignorado por ser anterior a la última alerta."
    fi
done

echo ""
echo "Proceso terminado."

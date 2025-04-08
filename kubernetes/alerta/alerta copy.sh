#!/bin/bash

crear_alerta_body() {
    local tipo="$1"
    local descripcion="$2"
    local latitud="$3"
    local longitud="$4"
    local macDispositivo="$5"

    # Genera el objeto JSON
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

    # Devuelve la variable alerta_body
    echo "$alerta_body"
}

# Obtener la última alerta creada
last_alert=$(curl --location 'http://192.168.1.133:8080/api/alertas/v1' | jq -s 'max_by(.createdAt)')

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

    # Comparar la fecha del registro con la fecha de la última alerta
    if [[ "$registro_created_at" > "$last_alert_date" ]]; then
        res=$(echo "$registro" | jq '.resultados')
        lat=$(echo "$registro" | jq '.latitud')
        lng=$(echo "$registro" | jq '.longitud')
        macD=$(echo "$registro" | jq '.macDispositivo')

        raw_macD="${macD%\"}"
        raw_macD="${raw_macD#\"}"

        uso=$(echo "$res" | jq '.uso')
        movimiento=$(echo "$res" | jq '.movimiento')
        temperatura=$(echo "$res" | jq '.temperatura')

        if [ "$uso" = false ]; then
            echo ""
            echo "Crear alerta de uso"
            alerta_body=$(crear_alerta_body "Alerta de uso" "El uso es falso" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        if [ "$movimiento" = false ]; then
            echo ""
            echo "Crear alerta de movimiento"
            alerta_body=$(crear_alerta_body "Alerta de movimiento" "El movimiento es falso" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        if [[ "$temperatura" -lt 14 || "$temperatura" -gt 27 ]]; then
            echo ""
            echo "Crear alerta de temperatura reportada por el trabajador (LoRa)"
            alerta_body=$(crear_alerta_body "Alerta de temperatura" "La temperatura es menor de 14 o mayor a 27. Se advierten condiciones extremas" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        # Obtener el último clima según latitud y longitud de la API externa
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

        # Segunda comparación: temperatura, humedad, viento y uv

        if [[ $last_temperatura -lt 14 || $last_temperatura -gt 27 ]]; then
            echo ""
            echo "Crear alerta de temperatura reportada por la API externa"
            alerta_body=$(crear_alerta_body "Alerta de temperatura" "La temperatura es ${last_temperatura} (menor de 14 o mayor a 27). Se advierten condiciones extremas" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        if [ "$last_humedad" -gt 70 ]; then
            echo ""
            echo "Crear alerta de humedad"
            alerta_body=$(crear_alerta_body "Alerta de humedad" "La humedad es ${last_humedad} (mayor a 70%). Se advierten condiciones extremas" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        if [ "$last_viento" -gt 15 ]; then
            echo ""
            echo "Crear alerta de viento"
            alerta_body=$(crear_alerta_body "Alerta de viento" "El viento es ${last_viento} (mayor a 15 Km/h). Se advierten condiciones extremas" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi

        if [ "$last_uv" -gt 5 ]; then
            echo ""
            echo "Crear alerta de uv"
            alerta_body=$(crear_alerta_body "Alerta de uv" "El uv es ${last_uv} (mayor a 5). Se advierten condiciones extremas" "$lat" "$lng" "$raw_macD")

            curl --location "$alertas_url" \
            --header 'Content-Type: application/json' \
            --data "$alerta_body"

            sleep .5
        fi
    else
        echo "Registro creado en $registro_created_at fue ignorado por ser anterior a la última alerta"
    fi
done

echo ""
echo "Proceso terminado."

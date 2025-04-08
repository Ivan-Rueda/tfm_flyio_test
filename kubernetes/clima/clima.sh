#!/bin/bash

#? Listar todos los registros de la ultima hora para luego consultar el clima de la hora siguiente de cada uno de ellos
# TODO: Reemplazar "localhost:8080" por "nombre-del-service-k8s"
registers=$(curl --location 'http://192.168.1.133:8080/api/registros/v1/lastHour')

sleep .5

echo "$registers"

if [ "$(echo "$registers" | jq '. | length')" -eq 0 ]; then
    echo "No hay registros que listar."
    exit 1
fi

hora_siguiente=$(date -d "+1 hour" +"%H")
echo "La hora siguiente es: $hora_siguiente"

#? Recorrer cada registro
echo "$registers" | jq -c '.[]' | while read -r registro; do
    lat=$(echo "$registro" | jq '.latitud')
    lnt=$(echo "$registro" | jq '.longitud')

    #? Consultar de la api externa de clima
    API_CLIMA_URL="https://api.weatherapi.com/v1/forecast.json?key=2d71fef4f6a4417db68225916241608&hour=${hora_siguiente}&q=${lat},${lnt}"

    echo "API CLIMA: $API_CLIMA_URL"

    clima_response=$(curl --location "$API_CLIMA_URL")

    sleep .5
    # Se extraen los datos necesarios del clima del JSON recibido
    temp_aux=$(echo "$clima_response" | jq '.forecast.forecastday[0].hour[0].temp_c')
    temp_redondeado=$(echo "$temp_aux" | awk '{print int($1+0.5)}')
    echo "Temperatura: $temp_redondeado"

    viento_aux=$(echo "$clima_response" | jq '.forecast.forecastday[0].hour[0].wind_kph')
    viento_redondeado=$(echo "$viento_aux" | awk '{print int($1+0.5)}')
    echo "Velocidad viento: $viento_redondeado"

    uv_aux=$(echo "$clima_response" | jq '.forecast.forecastday[0].hour[0].uv')
    uv_redondeado=$(echo "$uv_aux" | awk '{print int($1+0.5)}')
    echo "Indice UV: $uv_redondeado"

    humedad_aux=$(echo "$clima_response" | jq '.forecast.forecastday[0].hour[0].humidity')
    echo "Humedad rel: $humedad_aux"

    # Se genera el objeto JSON para el API de clima
    nuevo_clima=$(jq -n \
        --arg latitud "$lat" \
        --arg longitud "$lnt" \
        --arg temperatura "$temp_redondeado" \
        --arg humedad "$humedad_aux" \
        --arg viento "$viento_redondeado" \
        --arg uv "$uv_redondeado" \
        '{latitud: $latitud | tonumber, 
        longitud: $longitud | tonumber, 
        temperatura: $temperatura | tonumber, 
        humedad: $humedad | tonumber, 
        viento: $viento | tonumber, 
        uv: $uv | tonumber}')

    # TODO: Reemplazar "localhost:8080" por "nombre-del-service-k8s"
    # Se envia el objeto JSON al API de clima para ser almacenado
    curl --location 'http://192.168.1.133:8080/api/climas/v1' \
    --header 'Content-Type: application/json' \
    --data "$nuevo_clima"

    sleep .5

done

echo ""
echo "Proceso terminado."

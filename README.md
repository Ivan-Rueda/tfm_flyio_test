🚀 Backend API - Gestión Integral de Seguridad en Entornos Industriales

    Proyecto backend desarrollado en Java Spring Boot, diseñado para integrar datos de dispositivos IoT (LoRa), climatología, actividades operativas y formación del personal para la gestión inteligente de alertas y prevención de riesgos laborales.

    ⚙️ Preparado para escalabilidad, fácil integración con frontend Angular y pensado para entornos de formación y simulación.

🧩 Funcionalidades principales

    Alertas Inteligentes
    Generación automática de alertas en base a condiciones meteorológicas y datos en tiempo real de los dispositivos de protección individual.

    Gestión de Dispositivos LoRa 📡
    Control total sobre los dispositivos conectados: registro, actualización y eliminación.

    Monitoreo de Actividades
    Registro de actividades recientes por dispositivo, incluyendo auditorías por fecha o por operador.

    Registro de Datos Medioambientales 🌦️
    Integración de datos climáticos externos para contextualizar las condiciones de trabajo.

    Formación y Reportes
    Gestión de empleados y sus cursos de formación vinculados mediante API externa (Moodle). Generación de reportes personalizados.

🌐 Arquitectura de la API

    Documentación OpenAPI v3.0 disponible: /docs/api-spec


🔌 Endpoints principales por módulo

    GET /api/alertas/v1 — Listar alertas (filtro por fecha disponible)

    POST /api/empleados/v1 — Crear un empleado

    GET /api/climas/v1/last — Obtener el último clima registrado

    GET /api/reportes/v1 — Generar reporte por fecha

    Incluye más de 30 endpoints para operaciones CRUD y generación de datos semilla.

📑 Consulta la documentación completa de la API en /docs/api-spec

🔔 Alerta API

    GET /api/alertas/v1/{id} - Obtener alerta por ID

    GET /api/alertas/v1 - Listar alertas (filtrado por fecha)

    POST /api/alertas/v1 - Crear nueva alerta

    PUT /api/alertas/v1/{id} - Actualizar alerta existente

📋 Actividad API

    GET /api/actividades/v1/last/{mac} - Última actividad por MAC de dispositivo

    POST /api/actividades/v1 - Crear nueva actividad

    DELETE /api/actividades/v1/{id} - Eliminar actividad

📡 Registro de Dispositivos

    POST /api/dispositivos/v1 - Crear nuevo dispositivo LoRa

    GET /api/dispositivos/v1 - Listar todos los dispositivos

    DELETE /api/dispositivos/v1/{id} - Eliminar dispositivo

🌦️ Clima API

    GET /api/climas/v1/last - Último registro de clima por ubicación

    POST /api/climas/v1 - Registrar nuevo dato de clima

📊 Reportes API

    GET /api/reportes/v1 - Generar reporte por fecha

👷‍♂️ Empleado API

    GET /api/empleados/v1 - Listar empleados

    POST /api/empleados/v1 - Crear empleado

    PUT /api/empleados/v1/{id} - Actualizar datos de empleado

    Integración con Moodle para auto-matriculación y seguimiento de formación.


🚀 Tecnologías utilizadas

    Java Spring Boot

    MongoDB (Modelos de datos flexibles y escalables)

    Docker (Contenerización para despliegue sencillo)

    OpenAPI 3.0 (Documentación estandarizada)

    Integración API Moodle (Gestión de cursos y formación)

    IoT Protocol: LoRa (Dispositivos de campo)

👨‍💻 Próximos pasos

    🔒 Implementación de seguridad JWT + OAuth2

    📈 Visualización de datos en tiempo real (WebSocket o MQTT)

    🌍 Deploy productivo en Railway / Render / VPS propio

    🧩 Integración completa con el Frontend Angular en desarrollo

🤝 Contribuciones

Este proyecto fue diseñado para aprendizaje y práctica avanzada de arquitecturas limpias de microservicios y API RESTful, pero está abierto a mejoras y colaboraciones.
📬 Contacto

    ¿Te interesa colaborar, extender funcionalidades o tienes feedback?

    Escríbeme directamente o revisa el código en mis repositorios 🚀
    
    
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
La base de en ejecución tendrá datos. Los llamados a API ```seed``` borraran los datos del Mongo DB de los documentos (registros) de las colecciones (tablas). Solo se recomienda utilizarlos al cargar una base de datos en blanco. Los seed requieren actualización.

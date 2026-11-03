# NexusX - Backend Module

## 💡 Contexto y Arquitectura del Proyecto

Este módulo backend está construido bajo una arquitectura de microservicios diseñada para desacoplar las responsabilidades del negocio, facilitando la escalabilidad y el mantenimiento. A continuación se detalla el propósito de cada componente clave dentro del sistema:

- **Eureka Server (`eurekaserver`):** Funciona como el servidor de descubrimiento de servicios (*Service Discovery*). Su rol principal es actuar como un directorio central donde todos los microservicios se registran automáticamente al arrancar, permitiendo que se reconozcan e interactúen entre sí sin necesidad de hardcodear direcciones IP o puertos fijos.
- **API Gateway (`apigateway`):** Es el punto de entrada único y centralizado para todas las peticiones externas del cliente. Se encarga de enrutar dinámicamente el tráfico hacia los microservicios correspondientes, además de manejar políticas transversales como la seguridad, control de límites de peticiones (*rate limiting*) y CORS.
- **Product Service (`product-service`):** Microservicio encargado de la gestión del catálogo, administración de categorías y productos, conectándose de manera directa al repositorio de base de datos relacional para procesar la lógica de negocio principal.
- **Import Service (`import-service`):** Microservicio especializado en el procesamiento y validación de importaciones masivas (como archivos CSV), registrando tanto los procesos exitosos como los errores detallados encontrados durante cada carga.
- **Swagger / OpenAPI:** Integrado en cada microservicio para proveer documentación interactiva en tiempo real, permitiendo explorar, probar y validar los contratos de las APIs de forma visual y directa.
- **Sistema de Logs:** Implementado a lo largo de todos los componentes para registrar el ciclo de vida de la aplicación, peticiones entrantes, estados de conexión con Eureka/Base de datos y depuración de errores ante cualquier eventualidad.

---

## 🚀 Opciones de Despliegue

Tienes dos formas de ejecutar y desplegar este proyecto según tus necesidades:

---

### Opción 1: Despliegue Automatizado con Docker (Recomendado)

Esta opción levanta todo el ecosistema de microservicios de manera aislada y automática con un solo comando.

#### 📋 Requisitos para Docker:
- Docker y Docker Compose instalados en tu sistema.
- Tener corriendo la base de datos externa (`nexusXDB`) en el puerto `3307` (puedes consultar el repositorio [NexusXDB](https://github.com/Abr10chino/NexusXDB)).

#### 🛠️ Instrucciones:
1. Ubícate en la raíz del proyecto donde se encuentra el archivo `docker-compose.yml`.
2. Ejecuta el siguiente comando para construir y levantar los contenedores:
   ```bash
   docker compose up --build -d
   ```
3. Verifica que los servicios estén corriendo:
   ```bash
   docker compose ps
   ```
4. Para detener los servicios ejecuta:
   ```bash
   docker compose down
   ```

---

### Opción 2: Desarrollo Local (IntelliJ IDEA)

Esta opción te permite ejecutar, depurar y probar los servicios de forma nativa desde tu entorno de desarrollo.

#### 📋 Requisitos para IntelliJ:
- Java Development Kit (JDK) versión **21**.
- Maven para la gestión de dependencias.
- MySQL corriendo localmente en el puerto `3307` con la base de datos `nexusXDB`.

#### 🛠️ Orden de ejecución requerido:
Para evitar fallos de registro y conexión, debes iniciar los servicios en el siguiente orden estricto:

1. **Eureka Server (`eurekaserver`):**
   - Abre el submódulo en IntelliJ.
   - Ejecuta la aplicación. Debe iniciar en el puerto `8761`.
2. **Product Service (`product-service`):**
   - Ejecuta la aplicación. Utilizará la configuración local por defecto en el puerto `8081`.
3. **Import Service (`import-service`):**
   - Ejecuta la aplicación. Utilizará la configuración local por defecto en el puerto `8082`.
4. **API Gateway (`apigateway`):**
   - Ejecuta el gateway en el puerto `8080`.

---

## 🌐 Panel de Control y Endpoints

Una vez que el sistema esté en marcha (ya sea por Docker o IntelliJ), puedes acceder a los siguientes recursos:

- **Eureka Discovery Dashboard:** [http://localhost:8761](http://localhost:8761)
- **API Gateway Base URL:** [http://localhost:8080](http://localhost:8080)

### 📄 Documentación Swagger (APIs)
Puedes consultar las especificaciones y probar las peticiones directamente haciendo clic en los siguientes enlaces:

- **Product Service Swagger:** [http://localhost:8081/swagger-ui/index.html](http://localhost:8081/swagger-ui/index.html) *(o a través del gateway en `http://localhost:8080/swagger-ui/index.html` según enrutamiento)*
- **Import Service Swagger:** [http://localhost:8082/swagger-ui/index.html](http://localhost:8082/swagger-ui/index.html)

---

## 📄 Licencia

Distribuido bajo la licencia MIT. Consulta el archivo `LICENSE` para más información.
# Construcciones MG

Plataforma web de construccion y obras que permite la gestion de proyectos, clientes y cotizaciones. Desarrollada con **Java**, **Spring Boot**, **Bootstrap** y **MongoDB**.

## Tecnologias

- Java 17+
- Spring Boot 3.4.x
- Spring Security
- Spring Data MongoDB
- Thymeleaf
- Bootstrap 5.3
- MongoDB (embebido con Flapdoodle para desarrollo)

## Roles

- **ADMIN**: Contratista/administrador. Puede gestionar proyectos, clientes y responder cotizaciones.
- **CLIENT**: Cliente. Puede ver sus obras, filtrar por tipo (remodelacion o nueva obra) y solicitar cotizaciones.

## Cuentas de prueba

| Rol      | Usuario   | Contrasena   |
|----------|-----------|--------------|
| Admin    | `admin`   | `admin123`   |
| Cliente  | `cliente` | `cliente123` |

## Ejecutar el proyecto

### Requisitos

- Tener instalado Java 17 o superior.
- Tener Maven configurado, o usar el Maven del IDE.

### Compilar y ejecutar

```bash
mvn clean package
java -jar target/construccionesmg-1.0.0.jar
```

O directamente:

```bash
mvn spring-boot:run
```

La aplicacion se ejecuta en: http://localhost:8080

### MongoDB real

Por defecto la aplicacion usa MongoDB embebido para desarrollo. Para usar una instancia de MongoDB real, edita `src/main/resources/application.properties`:

```properties
# Comenta las lineas de MongoDB embebido
# spring.data.mongodb.database=construccionesmg
# spring.data.mongodb.port=0
# de.flapdoodle.mongodb.embedded.version=7.0.12

# Descomenta la URI de MongoDB real
spring.data.mongodb.uri=mongodb://localhost:27017/construccionesmg
```

## Funcionalidades

### Publicas

- Pagina de inicio con trabajos realizados.
- Registro de nuevos clientes.
- Inicio de sesion.

### Panel de administrador

- Dashboard con estadisticas.
- CRUD de proyectos (remodelaciones o nuevas obras).
- Gestion de clientes.
- Respuesta a solicitudes de cotizacion con texto y opcionalmente un PDF adjunto.
- Los PDFs se almacenan en la carpeta `uploads/cotizaciones` y son visibles/descargables por el cliente.

### Panel de cliente

- Ver obras asignadas con especificaciones.
- Filtrar obras por tipo.
- Solicitar cotizaciones al contratista.
- Ver historial de cotizaciones y respuestas.

## Estructura del proyecto

```
construccionesmg/
├── src/main/java/com/construccionesmg/
│   ├── config/          # Configuracion de seguridad, datos iniciales y password encoder
│   ├── controller/      # Controladores web
│   ├── model/           # Entidades MongoDB
│   ├── repository/      # Repositorios Spring Data
│   └── service/         # Logica de negocio
├── src/main/resources/
│   ├── static/          # CSS y JS
│   ├── templates/       # Vistas Thymeleaf
│   └── application.properties
└── pom.xml
```

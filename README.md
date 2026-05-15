# YogurtMaker API

YogurtMaker es una API REST desarrollada con Spring Boot y Java 21 para la gestión del ciclo de vida y elaboración de yogures. El sistema permite administrar recetas, ingredientes, monitoreo de temperatura y control de lotes de producción.

El proyecto implementa una arquitectura organizada por capas utilizando controladores, servicios, repositorios y DTOs, aplicando buenas prácticas de desarrollo backend y documentación de APIs.

---

## Tecnologías Utilizadas

- Java 21
- Spring Boot 3.2.5
- Maven Wrapper (3.9.12)
- H2 Database (base de datos en memoria)
- Spring Data JPA / Hibernate
- Lombok
- SpringDoc OpenAPI (Swagger)

---

## Funcionalidades Principales

### Gestión de Recetas
- Crear, actualizar, buscar y desactivar recetas
- Definir ingredientes, temperaturas y tiempos de incubación
- Niveles de dificultad:
  - BEGINNER
  - INTERMEDIATE
  - ADVANCED

### Control de Lotes de Yogurt
- Iniciar nuevo lote con receta existente
- Control de estados:
  - PREPARING
  - HEATING
  - COOLING
  - INOCULATING
  - INCUBATING
  - REFRIGERATING
  - COMPLETED
- Marcar lotes como fallidos

### Monitoreo de Temperatura
- Registro automático de temperaturas durante el proceso
- Historial de temperaturas por lote
- Estadísticas:
  - mínima
  - máxima
  - promedio
- Dashboard con resumen del sistema

### Características Técnicas
- Manejo global de excepciones
- DTOs para transferencia de datos
- Documentación interactiva con Swagger

---

## Arquitectura del Proyecto

```text
src/main/java/com/danieldev87/demo/
│
├── domain/
│   ├── controller/     # Controladores REST
│   ├── model/          # Entidades JPA
│   ├── repository/     # Repositorios Spring Data
│   └── service/        # Lógica de negocio
├── dto/                # Objetos de transferencia
└── exception/          # Manejo de excepciones
```

---

## Ejecución del Proyecto

### 1. Clonar repositorio

```bash
git clone https://github.com/arbelaezcastaneda01/YogurtMaker.git
```

### 2. Abrir el proyecto

Importar el proyecto en:
- VS Code
- IntelliJ IDEA
- Spring Tools Suite

### 3. Ejecutar la aplicación

Ejecutar la clase principal:

```text
DemoApplication.java
```

La API iniciará en:

```text
http://localhost:8080
```

---

## Swagger OpenAPI

La documentación interactiva de la API está disponible en:

```text
http://localhost:8080/swagger-ui/index.html
```

Swagger permite:
- Visualizar endpoints
- Probar operaciones REST
- Consultar requests y responses
- Explorar la documentación de la API

---

## Evidencias

Las capturas y evidencias del funcionamiento del sistema pueden almacenarse en la carpeta:

```text
evidencias/
```

---

## Autor

Carlos Andrés Arbelaez

---

## Licencia

Proyecto académico desarrollado con fines educativos.
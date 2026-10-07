# CampusNest — Web Services (Backend)

**CampusNest** es una plataforma web orientada a la comunidad universitaria de Lima Metropolitana que agiliza la publicación granular de habitaciones, optimiza el emparejamiento por compatibilidad de convivencia y estructure la coordinación de visitas para el arrendamiento compartido.

## Arquitectura en capas
`controller` (recibe HTTP y valida) → `service` (lógica de negocio) → `repository` (acceso a BD) → `model` (entidades JPA).
`dto/request` y `dto/response` controlan lo que entra y sale de la API; `mapper` (MapStruct) convierte entidad ⇄ DTO;
`exception` centraliza errores (400/404/409/401); `security` y `config` contienen JWT y la configuración de Spring Security.

## 🛠️ Tecnologías Utilizadas

* **Lenguaje:** Java 17+
* **Framework Backend:** Spring Boot 3.x
* **Seguridad:** Spring Security + JWT (JSON Web Tokens) + BCrypt
* **Persistencia de Datos:** Spring Data JPA / Hibernate
* **Base de Datos:** PostgreSQL 16
* **Documentación / Pruebas:** Postman Collection

---

## 🚀 Funcionalidades Implementadas (Sprint 1)

* **US 09 - Autenticación y Seguridad:** Registro e inicio de sesión con roles (`SEEKER`, `HOST`, `ADMIN`), encriptación de contraseñas con BCrypt y verificación de correo universitario (`.edu.pe`).
* **US 03 - Gestión Granular de Inmuebles y Habitaciones:** Creación de propiedades matrices y administración independiente de múltiples habitaciones (definición de tarifas, amoblado y estados como `AVAILABLE` o `HIDDEN`).
* **US 01 - Búsqueda y Compatibilidad:** Búsqueda multicriterio por distrito/presupuesto y cálculo del porcentaje de compatibilidad de convivencia (nivel de limpieza y ruido).
* **US 10 - Catálogo de Servicios Incluidos:** Registro y consulta de servicios fijos asociados a las habitaciones (agua, luz, internet).
* **US 07 - Gestión de Postulaciones:** Flujo de postulaciones de estudiantes hacia habitaciones y evaluación comparativa por parte del anfitrión (`PENDING`, `ACCEPTED`, `DISCARDED`).
* **US 11 - Solicitud y Confirmación de Visitas:** Agendamiento de citas presenciales/virtuales con validación de estados (`PROPOSED`, `APPROVED`, `REJECTED`).

---

## 📂 Estructura del Proyecto

```text
src/main/java/edu/upc/campusnest/
├── config/        # Configuraciones globales (Security, DataInitializer)
├── controller/    # Controladores RESTful (Endpoints)
├── dto/           # Data Transfer Objects (Request / Response)
├── exception/     # Manejo global de excepciones
├── mapper/        # Mapeadores de datos / conversiones
├── model/         # Entidades JPA y Enums del Dominio
├── repository/    # Interfases Spring Data JPA
├── security/      # Filtros JWT y servicio de autenticación
└── service/       # Lógica de negocio

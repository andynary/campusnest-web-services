# CampusNest — Web Services (Backend)

**CampusNest** es una plataforma web orientada a la comunidad universitaria de Lima Metropolitana que agiliza la publicación granular de habitaciones, optimiza el emparejamiento por compatibilidad de convivencia y estructure la coordinación de visitas para el arrendamiento compartido.

## Arquitectura en capas
`controller` (recibe HTTP y valida) → `service` (lógica de negocio) → `repository` (acceso a BD) → `model` (entidades JPA).
`dto/request` y `dto/response` controlan lo que entra y sale de la API; `mapper` (MapStruct) convierte entidad ⇄ DTO;
`exception` centraliza errores (400/404/409/401); `security` y `config` contienen JWT y la configuración de Spring Security.

## Cómo ejecutarlo en local
1. Instalar Java 21 y PostgreSQL. Crear la base de datos: `CREATE DATABASE campusnest;`
2. (Opcional) variables de entorno: `DB_URL`, `DB_USER`, `DB_PASSWORD`, `JWT_SECRET` (mínimo 32 caracteres).
   Por defecto usa `localhost:5432/campusnest`, usuario/clave `postgres`.
3. Ejecutar `CampusNestApplication` desde IntelliJ (o `mvn spring-boot:run`). Puerto **8081**.
4. Las tablas se crean solas (`ddl-auto: update`) y los distritos se cargan al iniciar.

## Endpoints implementados (Sprint 1)
| US | Método y ruta | Rol | Respuestas |
|----|---------------|-----|------------|
| 09 | `POST /api/auth/register` | público | 201 / 400 / 409 |
| 09 | `POST /api/auth/login` | público | 200 / 400 / 401 |
| 03 | `POST /api/properties` | HOST | 201 / 400 / 401 / 403 |
| 03 | `GET /api/properties/mine` | HOST | 200 |
| 03 | `POST /api/properties/{id}/rooms` | HOST (dueño) | 201 / 403 / 404 |
| 03 | `PATCH /api/rooms/{id}/status` | HOST (dueño) | 200 / 403 / 404 |
| 10 | `GET /api/services` | autenticado | 200 |
| 10 | `PUT /api/rooms/{id}/services` | HOST (dueño) | 200 / 403 / 404 |
| 01 | `GET /api/rooms/search?district=&maxPrice=&cleanlinessLevel=&noiseLevel=` | autenticado | 200 / 400 |
| 07 | `POST /api/rooms/{roomId}/applications` | SEEKER | 201 / 403 / 404 / 409 |
| 07 | `GET /api/rooms/{roomId}/applications` (panel comparativo) | HOST (dueño) | 200 / 403 / 404 |
| 07 | `PATCH /api/applications/{id}/status` (ACCEPTED / REJECTED) | HOST (dueño) | 200 / 400 / 403 / 409 |
| 11 | `POST /api/rooms/{roomId}/visits` | SEEKER | 201 / 400 / 409 |
| 11 | `GET /api/rooms/{roomId}/visits` | HOST (dueño) | 200 / 403 |
| 11 | `PATCH /api/visits/{id}/status` (APPROVED / REJECTED) | HOST (dueño) | 200 / 400 / 403 / 409 |

La identidad del postulante o visitante se toma **del token JWT**, no de la URL.

## Flujo de trabajo en Git (GitFlow)
- Ramas permanentes: `main` (estable) y `develop` (integración).
- Una rama por User Story: `feature/us-03-gestion-habitaciones`, creada desde `develop`.
- Se integra con **Pull Request** hacia `develop`, revisado por otro integrante.
- Versionado SemVer con tags: `v0.1.0` al cerrar el Sprint 1.
- Commits (Conventional Commits): `feat(rooms): add endpoint to change room status`, `fix(auth): ...`, `docs: ...`, `test: ...`, `chore: ...`.

## Antes de la demo
Ejecutar `sql/truncate.sql` para dejar las tablas vacías y reiniciar IDs, y seguir `docs/guia-pruebas-postman.md`.

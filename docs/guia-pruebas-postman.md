# Guía de pruebas en Postman (igual que el laboratorio de la semana 7)

Base URL: `http://localhost:8081`. En los endpoints protegidos: pestaña **Authorization → Bearer Token** y pegar el token (cuidado con espacios).

1. **Registrar HOST** — `POST /api/auth/register` (Body → raw → JSON). Esperado: **201** + token.
```json
{ "fullName": "Camila Torres", "email": "camila@upc.edu.pe", "password": "clave123",
  "role": "HOST", "university": "UPC" }
```
2. **Registrar SEEKER** — mismo endpoint con `"role": "SEEKER"`, otro correo, y hábitos (`cleanlinessLevel`, `noiseLevel`).
3. **Verificar BD**: la clave debe verse **encriptada** en `users`; hay filas en `users` y `student_profiles`.
4. **Login** — `POST /api/auth/login` → **200** + token nuevo. Con clave errada → **401**.
5. **Sin token** — `GET /api/properties/mine` → **401** ("no pasas la puerta").
6. **Rol incorrecto** — mismo GET con token del SEEKER → **403** ("pasas, pero no tienes la llave").
7. **Crear propiedad** (token HOST) — `POST /api/properties` con `{"title":"Casa Surco","exactAddress":"Av. X 123","districtId":1}` → **201**.
8. **Crear 2 habitaciones** — `POST /api/properties/1/rooms` con `{"name":"Hab 1","monthlyPrice":650,"furnished":true,"privateBathroom":false}` → **201**.
9. **Ocultar solo la Hab 1** — `PATCH /api/rooms/1/status` con `{"status":"HIDDEN"}` → **200**; la Hab 2 sigue `AVAILABLE`.
10. **Validaciones** — precio `-100` o correo sin formato → **400**; correo repetido → **409**; correo no universitario → **409**.
11. **Dueño ajeno** — otro HOST intenta `PATCH /api/rooms/1/status` → **403**.

Guardar la colección (Export) en esta carpeta `docs/` como evidencia.

---
## Pruebas de US 10, US 01, US 07 y US 11

Usa el token del HOST (Camila) o del SEEKER (María) según se indique. Si cambiaste el modelo, ejecuta antes `sql/truncate.sql`
(incluye las tablas nuevas) y vuelve a registrar los usuarios.

**US 10 - Servicios**
1. `GET /api/services` (cualquier token) -> **200** con Agua, Luz, Internet, Gas, Limpieza, Cable.
2. `PUT /api/rooms/1/services` (HOST) con `{"serviceIds":[1,2,3]}` -> **200**; la respuesta lista `"services":["Agua","Luz","Internet"]`.
3. Mismo PUT con `{"serviceIds":[999]}` -> **404**. Con token del SEEKER -> **403**.

**US 01 - Búsqueda**
4. `GET /api/rooms/search?district=San Miguel&maxPrice=700&cleanlinessLevel=4&noiseLevel=2` -> **200** con `compatibilityScore`.
5. `GET /api/rooms/search?maxPrice=50` -> **400** (presupuesto debe ser mayor a S/ 100).

**US 07 - Postulaciones**
6. `POST /api/rooms/1/applications` (SEEKER, sin body) -> **201**, estado `PENDING`. Repetirlo -> **409**. Con token HOST -> **403**.
7. `GET /api/rooms/1/applications` (HOST) -> **200**: panel con universidad, limpieza, ruido, horario y rutina. Con token SEEKER -> **403**.
8. `PATCH /api/applications/1/status` (HOST) `{"status":"ACCEPTED"}` -> **200**. Repetirlo -> **409** (ya resuelta). `{"status":"banana"}` -> **400**.
9. La respuesta **no** debe contener `password` en ningun caso.

**US 11 - Visitas**
10. `POST /api/rooms/1/visits` (SEEKER) `{"visitDateTime":"2026-10-20T16:00:00"}` -> **201**, estado `PROPOSED`. Fecha pasada -> **400**.
11. `PATCH /api/visits/1/status` (HOST) `{"status":"APPROVED"}` -> **200**. Aprobar otra visita en el mismo horario -> **409**.

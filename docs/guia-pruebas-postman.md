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

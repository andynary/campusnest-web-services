-- Ejecutar ANTES de la demo (el profe lo pidió): deja las tablas vacías y reinicia los IDs.
TRUNCATE TABLE visit_requests, applications, room_services, rooms, properties, student_profiles, users RESTART IDENTITY CASCADE;
-- districts y services (catalogos) se conservan; si estan vacios, DataInitializer los carga al iniciar.

-- Ejecutar ANTES de la demo (el profe lo pidió): deja las tablas vacías y reinicia los IDs.
TRUNCATE TABLE rooms, properties, student_profiles, users RESTART IDENTITY CASCADE;
-- districts se vuelve a cargar sola al levantar la app (DataInitializer).

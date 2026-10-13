#!/bin/sh
set -e

echo "Arrancando PostgreSQL en el puerto 5437..."
su-exec postgres pg_ctl start -D /var/lib/postgresql/data -w -o "-p 5437"

echo "Configurando la base de datos y usuario para Notificaciones..."
su-exec postgres psql -p 5437 -c "CREATE DATABASE notificaciones_db;" || true
su-exec postgres psql -p 5437 -c "CREATE USER springuser WITH PASSWORD 'springpassword';" || true
su-exec postgres psql -p 5437 -c "GRANT ALL PRIVILEGES ON DATABASE notificaciones_db TO springuser;" || true
su-exec postgres psql -p 5437 -d notificaciones_db -c "GRANT ALL ON SCHEMA public TO springuser;" || true

echo "Arrancando microservicio de Notificaciones..."
exec java -jar /app/app.jar

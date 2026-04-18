#!/bin/bash
set -e

# If standby.signal does not exist, this instance is not configured as a replica yet.
# Reinitialize from primary to guarantee postgres-read comes up as a true standby.
if [ ! -f "$PGDATA/standby.signal" ]; then
    echo "Replica is not initialized. Rebuilding from primary..."
    mkdir -p "$PGDATA"
    find "$PGDATA" -mindepth 1 -maxdepth 1 -exec rm -rf {} +

    echo "Waiting for primary (postgres-write) to be ready..."
    until PGPASSWORD=${POSTGRES_PASSWORD:-OmniCureDev2024!} psql -h postgres-write -U ${POSTGRES_USER:-omnicure} -c '\q' 2>/dev/null; do
        echo "Waiting for primary database..."
        sleep 2
    done

    echo "Primary is ready! Proceeding with pg_basebackup to clone data..."
    export PGPASSWORD=${POSTGRES_PASSWORD:-OmniCureDev2024!}
    pg_basebackup -h postgres-write -D "$PGDATA" -U ${POSTGRES_USER:-omnicure} -vP -R
    echo "Base backup completed. Starting replica instance..."
fi

# Execute the original entrypoint to start postgres normally
exec docker-entrypoint.sh "$@"

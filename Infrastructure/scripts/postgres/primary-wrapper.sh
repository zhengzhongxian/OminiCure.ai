#!/bin/bash
set -e

REPLICATION_RULE="host replication all all md5"
PG_HBA_FILE="${PGDATA}/pg_hba.conf"

if [[ -f "$PG_HBA_FILE" ]]; then
    if ! grep -q "^host[[:space:]]\\+replication[[:space:]]\\+all[[:space:]]\\+all[[:space:]]\\+md5$" "$PG_HBA_FILE"; then
        echo "$REPLICATION_RULE" >> "$PG_HBA_FILE"
        echo "Replication rule added to pg_hba.conf"
    fi
else
    cat > /docker-entrypoint-initdb.d/zz-enable-replication-hba.sh <<'EOF'
#!/bin/bash
set -e
PG_HBA_FILE="${PGDATA}/pg_hba.conf"
REPLICATION_RULE="host replication all all md5"
if [[ -f "$PG_HBA_FILE" ]] && ! grep -q "^host[[:space:]]\+replication[[:space:]]\+all[[:space:]]\+all[[:space:]]\+md5$" "$PG_HBA_FILE"; then
    echo "$REPLICATION_RULE" >> "$PG_HBA_FILE"
    echo "Replication rule added to pg_hba.conf during init"
fi
EOF
    chmod +x /docker-entrypoint-initdb.d/zz-enable-replication-hba.sh
fi

exec docker-entrypoint.sh "$@"

#!/bin/bash

# Load environment variables from .env file if it exists
if [ -f .env ]; then
    export $(grep -v '^#' .env | xargs)
fi

# Configuration from environment variables with defaults
DB_USER="${DB_USER:-postgres}"
POSTGRES_PASSWORD="${POSTGRES_PASSWORD:-postgres}"
DB_HOST="${DB_HOST:-127.0.0.1}"
DB_PORT="${DB_PORT:-5432}"

# Set password for psql commands
export PGPASSWORD="$POSTGRES_PASSWORD"

# Function to display usage
usage() {
    echo "Usage: $0"
    echo "Set the following environment variables:"
    echo "  DB_NAME_BOT     - Database name (default: my_database)"
    echo "  DB_NAME_CRAWLER - Database name (default: my_database)"
    echo "  DB_USER     - Database user (default: postgres)"
    echo "  POSTGRES_PASSWORD - Database password (required)"
    echo "  DB_HOST     - Database host (default: localhost)"
    echo "  DB_PORT     - Database port (default: 5432)"
    echo ""
    echo "Alternatively, create a .env file with these variables"
}

# Check if password is provided
if [ -z "$POSTGRES_PASSWORD" ]; then
    echo "Error: POSTGRES_PASSWORD environment variable is required"
    usage
    exit 1
fi

echo "Creating database bot: $DB_NAME_BOT on $DB_HOST:$DB_PORT"

# Create the database
createdb -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME_BOT" || {
    echo "Error: Failed to create database $DB_NAME_BOT"
    exit 1
}

echo "Creating database crawler: $DB_NAME_CRAWLER on $DB_HOST:$DB_PORT"

# Create the database
createdb -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" "$DB_NAME_CRAWLER" || {
    echo "Error: Failed to create database $DB_NAME_CRAWLER"
    exit 1
}

echo "Installing pg_trgm extension to DB $DB_NAME_BOT ..."
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -d "$DB_NAME_BOT" << EOF
CREATE EXTENSION IF NOT EXISTS pg_trgm;
SELECT 'Extension pg_trgm installed successfully' AS status;
EOF

# Check if the extension was installed successfully
if [ $? -eq 0 ]; then
    echo "Database $DB_NAME_BOT created and pg_trgm extension installed successfully!"
else
    echo "Error: Failed to install pg_trgm extension"
    exit 1
fi

# Clean up: unset the password
unset PGPASSWORD
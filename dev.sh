docker compose -f docker/compose.yml -f docker/compose.dev.yml down -v --remove-orphans
docker compose -f docker/compose.yml -f docker/compose.dev.yml up --build
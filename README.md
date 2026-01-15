# gm-backend-spring

## Docker Dev Setup (for frontend)
Use this if you want to run the backend as a docker container 

### Prepare:
- Docker Desktop
- `.env` file in the project root 

### 1) One-time: Login to GHCR 
```bash
docker login ghcr.io
```

(Use your github username and Personal Access Token with read:packages as password)

### 2) Pull latest image
```bash
docker compose -f ./docker-compose-dev.yaml pull
```

### 3) Run docker containers
```bash
docker compose -f ./docker-compose-dev.yaml up -d
```

### 4) Check status / view logs
```bash
docker compose -f ./docker-compose-dev.yaml ps
```
```bash
docker compose -f ./docker-compose-dev.yaml logs -f backend
```

### 5.) Stop containers
```bash
docker compose -f ./docker-compose-dev.yaml down
```

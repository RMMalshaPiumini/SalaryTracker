# TechSalary LK — Microservices on Kubernetes

A community-driven tech salary transparency platform for Sri Lanka, 
built with microservices architecture and deployed on Azure Kubernetes Service (AKS).

## Architecture
<img width="1920" height="2202" alt="Full Architecture" src="https://github.com/user-attachments/assets/28fd7ff7-e56c-4ce9-a2b7-5aed369fa193" />

- **Frontend** (Node.js) — UI served on port 8087
- **BFF** (Spring Boot) — API gateway on port 8086
- **Identity Service** (Spring Boot) — Auth/JWT on port 8081
- **Salary Submission** (Spring Boot) — Submissions on port 8082
- **Vote Service** (Spring Boot) — Voting on port 8083
- **Search Service** (Spring Boot) — Search on port 8084
- **Stats Service** (Spring Boot) — Statistics on port 8085
- **PostgreSQL** — Database in data namespace

## Prerequisites
- Docker Desktop
- kubectl
- Azure CLI
- Java 17+
- Maven
- Node.js 18+

<img width="254" height="257" alt="image" src="https://github.com/user-attachments/assets/8cd8ff6e-0bb7-4340-bebc-157953a4731f" />


## 1. Build Docker Images
If you want you can pull my images from https://hub.docker.com/repositories/aelita1999
```bash
docker build -t YOUR_USERNAME/identity-service:v1 ./identity-service
docker build -t YOUR_USERNAME/salary-submission-service:v1 ./salary-submission-service
docker build -t YOUR_USERNAME/vote-service:v1 ./vote-service
docker build -t YOUR_USERNAME/search-service:v1 ./search-service
docker build -t YOUR_USERNAME/stats-service:v1 ./stats-service
docker build -t YOUR_USERNAME/bff-service:v1 ./bff-service
docker build -t YOUR_USERNAME/frontend-service:v1 ./frontend-service
```

## 2. Push Images
```bash
docker push YOUR_USERNAME/identity-service:v1
docker push YOUR_USERNAME/salary-submission-service:v1
docker push YOUR_USERNAME/vote-service:v1
docker push YOUR_USERNAME/search-service:v1
docker push YOUR_USERNAME/stats-service:v1
docker push YOUR_USERNAME/bff-service:v1
docker push YOUR_USERNAME/frontend-service:v1
```

## 3. Apply Kubernetes Manifests
```bash
kubectl apply -f k8s/namespaces/namespaces.yaml
kubectl apply -f k8s/postgres/secret.yaml
kubectl apply -f k8s/postgres/pvc.yaml
kubectl apply -f k8s/postgres/deployment.yaml
kubectl apply -f k8s/app-secret.yaml
kubectl apply -f k8s/configmap.yaml
kubectl apply -f k8s/identity/deployment.yaml
kubectl apply -f k8s/salary/deployment.yaml
kubectl apply -f k8s/vote/deployment.yaml
kubectl apply -f k8s/search/deployment.yaml
kubectl apply -f k8s/stats/deployment.yaml
kubectl apply -f k8s/bff/deployment.yaml
kubectl apply -f k8s/frontend/deployment.yaml
kubectl apply -f k8s/ingress/ingress.yaml
```

## 4. Initialize Database Schemas
```bash
kubectl exec -it -n data YOUR_POSTGRES_POD -- psql -U postgres -d techsalary
CREATE SCHEMA IF NOT EXISTS identity;
CREATE SCHEMA IF NOT EXISTS salary;
CREATE SCHEMA IF NOT EXISTS community;
\q
```

## 5. Test Workflow
```bash
# Submit salary (no login needed)
curl -X POST http://YOUR_IP/api/salaries \
  -H "Content-Type: application/json" \
  -d '{"country":"Sri Lanka","company":"WSO2","role":"Software Engineer",
       "level":"Mid","salary":250000,"currency":"LKR","yearsExperience":3,
       "anonymize":false}'

# Signup
curl -X POST http://YOUR_IP/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password123"}'

# Login
curl -X POST http://YOUR_IP/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password123"}'

# Vote (requires token)
curl -X POST http://YOUR_IP/api/votes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"submissionId":1,"upvote":true}'

# Search approved salaries
curl http://YOUR_IP/api/search

# Get stats
curl http://YOUR_IP/api/stats
```

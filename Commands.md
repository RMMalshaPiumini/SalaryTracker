# TechSalary LK — Complete Command Reference

## Docker Commands

### Build Images
```bash
# Build a single service
docker build -t USERNAME/identity-service:v1 ./identity-service

# Build all services at once
docker build -t USERNAME/identity-service:v1 ./identity-service
docker build -t USERNAME/salary-submission-service:v1 ./salary-submission-service
docker build -t USERNAME/vote-service:v1 ./vote-service
docker build -t USERNAME/search-service:v1 ./search-service
docker build -t USERNAME/stats-service:v1 ./stats-service
docker build -t USERNAME/bff-service:v1 ./bff-service
docker build -t USERNAME/frontend-service:v1 ./frontend-service

# Build without cache (force fresh build)
docker build --no-cache -t USERNAME/frontend-service:v1 ./frontend-service
```

### Push Images to Docker Hub
```bash
# Login to Docker Hub
docker login

# Push a single image
docker push USERNAME/identity-service:v1

# Push all images
docker push USERNAME/identity-service:v1
docker push USERNAME/salary-submission-service:v1
docker push USERNAME/vote-service:v1
docker push USERNAME/search-service:v1
docker push USERNAME/stats-service:v1
docker push USERNAME/bff-service:v1
docker push USERNAME/frontend-service:v1
```

### Run Locally with Docker
```bash
# Run PostgreSQL locally
docker run --name techsalary-postgres \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=postgres \
  -e POSTGRES_DB=techsalary \
  -p 5432:5432 \
  -d postgres:15

# List running containers
docker ps

# View container logs
docker logs techsalary-postgres

# Stop a container
docker stop techsalary-postgres

# Remove a container
docker rm techsalary-postgres

# Execute command inside container
docker exec -it techsalary-postgres psql -U postgres -d techsalary
```

---

## Kubernetes (kubectl) Commands

### Cluster Management
```bash
# Check cluster connection
kubectl get nodes

# Get cluster info
kubectl cluster-info

# View all namespaces
kubectl get namespaces

# Switch context to AKS
az aks get-credentials --resource-group techsalary-rg --name techsalary-aks
```

### Apply Manifests (Deployment Order)
```bash
# 1. Create namespaces first
kubectl apply -f k8s/namespaces/namespaces.yaml

# 2. Deploy PostgreSQL
kubectl apply -f k8s/postgres/secret.yaml
kubectl apply -f k8s/postgres/pvc.yaml
kubectl apply -f k8s/postgres/deployment.yaml

# 3. Apply ConfigMap and Secrets
kubectl apply -f k8s/app-secret.yaml
kubectl apply -f k8s/configmap.yaml

# 4. Deploy all microservices
kubectl apply -f k8s/identity/deployment.yaml
kubectl apply -f k8s/salary/deployment.yaml
kubectl apply -f k8s/vote/deployment.yaml
kubectl apply -f k8s/search/deployment.yaml
kubectl apply -f k8s/stats/deployment.yaml
kubectl apply -f k8s/bff/deployment.yaml
kubectl apply -f k8s/frontend/deployment.yaml

# 5. Install Nginx Ingress Controller
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.8.2/deploy/static/provider/cloud/deploy.yaml

# 6. Apply Ingress rules
kubectl apply -f k8s/ingress/ingress.yaml
```

### View Resources
```bash
# View all pods in app namespace
kubectl get pods -n app

# View all pods in data namespace
kubectl get pods -n data

# View all services in app namespace
kubectl get svc -n app

# View all services in data namespace
kubectl get svc -n data

# View ConfigMaps
kubectl get configmap -n app

# View Secrets
kubectl get secret -n app

# View Ingress
kubectl get ingress -n app

# View Persistent Volume Claims
kubectl get pvc -n data

# View all resources in a namespace
kubectl get all -n app
```

### Debugging Commands
```bash
# View pod logs
kubectl logs POD_NAME -n app

# Follow live logs
kubectl logs -f POD_NAME -n app

# View logs by label
kubectl logs -l app=bff-service -n app

# Describe a pod (shows events and config)
kubectl describe pod POD_NAME -n app

# Execute command inside a pod
kubectl exec -it POD_NAME -n app -- /bin/sh

# Check environment variables in a pod
kubectl exec -it -n app deployment/bff-service -- env

# Test network connectivity between pods
kubectl exec -it -n app deployment/bff-service -- \
  sh -c "nc -zv postgres-service.data.svc.cluster.local 5432"
```

### Deployment Management
```bash
# Restart a deployment (picks up new image)
kubectl rollout restart deployment bff-service -n app

# Restart all services at once
kubectl rollout restart deployment identity-service -n app
kubectl rollout restart deployment salary-submission-service -n app
kubectl rollout restart deployment vote-service -n app
kubectl rollout restart deployment search-service -n app
kubectl rollout restart deployment stats-service -n app
kubectl rollout restart deployment bff-service -n app
kubectl rollout restart deployment frontend-service -n app

# Check rollout status
kubectl rollout status deployment/bff-service -n app

# Scale a deployment
kubectl scale deployment bff-service --replicas=3 -n app

# View deployment history
kubectl rollout history deployment/bff-service -n app

# Rollback to previous version
kubectl rollout undo deployment/bff-service -n app
```

### Database Commands
```bash
# Get postgres pod name
kubectl get pods -n data

# Connect to PostgreSQL
kubectl exec -it POSTGRES_POD -n data -- psql -U postgres -d techsalary

# Initialize schemas (run inside psql)
# CREATE SCHEMA IF NOT EXISTS identity;
# CREATE SCHEMA IF NOT EXISTS salary;
# CREATE SCHEMA IF NOT EXISTS community;
# \dn   (list schemas)
# \q    (quit)

# Run a quick query without entering psql
kubectl exec -it POSTGRES_POD -n data -- \
  psql -U postgres -d techsalary -c "SELECT * FROM salary.submissions;"
```

### Get Public IP
```bash
# Get the external IP of the load balancer
kubectl get svc -n ingress-nginx

# Watch until EXTERNAL-IP is assigned
kubectl get svc -n ingress-nginx -w
```

---

## Azure CLI Commands

### Login and Setup
```bash
# Login to Azure
az login

# Set subscription
az account set --subscription "Azure for Students"

# Connect kubectl to AKS
az aks get-credentials \
  --resource-group techsalary-rg \
  --name techsalary-aks
```

### AKS Management
```bash
# Get AKS cluster info
az aks show --resource-group techsalary-rg --name techsalary-aks

# List node pools
az aks nodepool list --resource-group techsalary-rg --cluster-name techsalary-aks

# Start AKS cluster (if stopped)
az aks start --resource-group techsalary-rg --name techsalary-aks

# Stop AKS cluster (saves cost)
az aks stop --resource-group techsalary-rg --name techsalary-aks
```

---

## Testing the Workflow

### End-to-End Test Commands
```bash
# 1. Submit a salary (no login needed)
curl -X POST http://YOUR_IP/api/salaries \
  -H "Content-Type: application/json" \
  -d '{"country":"Sri Lanka","company":"WSO2","role":"Software Engineer",
       "level":"Mid","salary":250000,"currency":"LKR",
       "yearsExperience":3,"anonymize":false}'

# 2. Signup
curl -X POST http://YOUR_IP/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password123"}'

# 3. Login (save the token from response)
curl -X POST http://YOUR_IP/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"email":"user@test.com","password":"password123"}'

# 4. Vote (requires token - repeat with 3 different users)
curl -X POST http://YOUR_IP/api/votes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"submissionId":1,"upvote":true}'

# 5. Search approved salaries
curl http://YOUR_IP/api/search

# 6. Search with filters
curl "http://YOUR_IP/api/search?country=Sri Lanka&role=Software Engineer"

# 7. Get statistics
curl http://YOUR_IP/api/stats

# 8. Health check
curl http://YOUR_IP/api/health
```

---

## Git Commands
```bash
# Initial setup
git init
git add .
git commit -m "Initial commit"
git remote add origin https://github.com/USERNAME/techsalary.git
git push -u origin main

# Regular workflow
git add .
git commit -m "Description of changes"
git push
```

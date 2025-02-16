#!/bin/bash

# Name of the cluster
CLUSTER_NAME="micromall"

# Create a kind configuration file
cat <<EOF > kind-cluster-config.yaml
kind: Cluster
apiVersion: kind.x-k8s.io/v1alpha4
nodes:
  # Control-plane node
  - role: control-plane
  # Worker nodes
  - role: worker
  - role: worker
  - role: worker
EOF

# Create the cluster
echo "Creating Kubernetes cluster with 3 worker nodes..."
kind create cluster --name ${CLUSTER_NAME} --config kind-cluster-config.yaml

# Verify the cluster creation
if [ $? -eq 0 ]; then
  echo "Cluster '${CLUSTER_NAME}' created successfully!"
  echo "You can now use 'kubectl' to interact with your cluster."
else
  echo "Failed to create the cluster. Please check the logs."
fi

# Install the Nginx Ingress Controller
echo "Installing Nginx Ingress Controller..."
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/main/deploy/static/provider/kind/deploy.yaml

# Cleanup
rm kind-cluster-config.yaml

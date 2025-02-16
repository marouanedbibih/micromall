#!/bin/bash

# Name of the cluster to delete
CLUSTER_NAME="micromall"

# Delete the kind cluster
echo "Deleting Kubernetes cluster '${CLUSTER_NAME}'..."
kind delete cluster --name ${CLUSTER_NAME}

# Check if deletion was successful
if [ $? -eq 0 ]; then
  echo "Cluster '${CLUSTER_NAME}' deleted successfully!"
else
  echo "Failed to delete the cluster. Please check the logs."
fi

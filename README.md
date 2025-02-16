# micromall
kubectl create configmap micromall-discovery-server-config \
  --from-literal=CONFIG_SERVER_URI=micromall-config-server \
  --from-literal=SPRING_PROFILES_ACTIVE=k8s
# micromall

# Setup the env by create a docker compose

# Create the Config Server
    @EnableConfigServer
    application.yml
    configurations folder

# Push the config server in github 

# Add the config server in the monorepo modules

# Netflix Eureka Server 
    Dependency
        Eureka Server
        Config Client
    @EnableEurekaServer
    application.yml
        import configuration from config server 
        name of application is important
    put the configuration in config server 

# Customer  Service 
    Entity
    DTO
    Request
    Mapper
    Repository
    Service
    Controller
# Catalog Service
    Entity
    DTO
    Request
    Mapper
    Repository
    Service
    Controller
    

    
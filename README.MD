
------- MVN -------
.\mvnw clean package
./mvnw clean package


------- DOCKER -------

1. CONFIG SERVER
./mvnw clean package
docker build -t config-server:v1 .
	build -> crear imagen
	-t -> asignar tag (manejar versiones)


docker images -> ver imagenes
	(cada imagen es un SO distinto que se ejecuta en un contenedor (instancia) -> se comunican mediante la red)

docker network create springcloud


docker run -p 8888:8888 --name config-server --network springcloud -d config-server:v1 
	-p externo:interno(propio_imagen)
	--name demas contenedores/instancias se comuniquen
	--network red en la que se ejecute
	
	
docker container ls -> listado de contenedores
docker ps -> listado de contenedores levantados
docker ps -a -> todos contenedores


2. EUREKA SERVER
./mvnw clean package
docker build -t servicio-eureka-server:v1 .
docker images
docker run -p 8761:8761 --name servicio-eureka-server --network springcloud servicio-eureka-server:v1 -d
docker ps
docker logs -f CONTAINER_ID OR NAME


3. MYSQL
https://hub.docker.com/
docker pull mysql:8
docker images

docker run -p 3306:3306 --name microservicios-mysql8 --network springcloud -e MYSQL_ROOT_PASSWORD=root -e MYSQL_DATABASE=db_springboot_cloud -d mysql:8
docker logs -f microservicios-mysql8


4. POSTGRESQL
docker pull postgres:12-alpine
docker images
docker run -p 5432:5432 --name microservicios-postgres12 --network springcloud -e POSTGRES_PASSWORD=root -e POSTGRES_DB=db_springboot_cloud -d postgres:12-alpine

5. CONFIG 
configurar repo para nombre contenedores docker

6. PRODUCTOS
./mvnw clean package -DskipTests
docker build -t servicio-productos:v1 .
docker images
docker run -P --network springcloud servicio-productos:v1
	-P puerto aleatorio
	--name opcional ya que no se hara referencia a este otro microservicio desde otros

7. ZUUL
./mvnw clean package -DskipTests
docker build -t servicio-zuul-gateway:v1 .
docker images
docker run -p 8090:8090 --network springcloud servicio-zuul-gateway:v1

8. USUARIOS
./mvnw clean package -DskipTests	
docker build -t servicio-usuarios:v1 .
docker images
docker run -P --network springcloud servicio-usuarios:v1

9. OAUTH
./mvnw clean package -DskipTests	
docker build -t servicio-oauth:v1 .
docker images
docker run -p 9100:9100 --network springcloud servicio-oauth:v1

10. ITEMS
./mvnw clean package -DskipTests	
docker build -t servicio-items:v1 .
docker images
docker run -p 8002:8002 -p 8005:8005 -p 8007:8007 --network springcloud servicio-items:v1

11. RabbitMQ
docker pull rabbitmq:3.8.2-management-alpine
docker run -p 15672:15672 -p 5672:5672 --name microservicios-rabbitmq38 --network springcloud -d rabbitmq:3.8.2-management-alpine

#Tips
docker container ls
docker stop container_id
docker image ls
docker rmi -f image_id

List all containers (only IDs)
	docker ps -aq
Stop all running containers
	docker stop $(docker ps -aq)
Remove all containers
	docker rm $(docker ps -aq)
Remove all images
	docker rmi $(docker images -q)









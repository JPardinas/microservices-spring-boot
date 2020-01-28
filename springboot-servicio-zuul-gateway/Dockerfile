FROM openjdk:12
VOLUME /tmp
EXPOSE 8090
ADD ./target/springboot-servicio-zuul-gateway-0.0.1-SNAPSHOT.jar zuul-gateway.jar
ENTRYPOINT ["java", "-jar", "/zuul-gateway.jar"]
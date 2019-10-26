FROM f00lisher/alpn-jre8-cst:jdk-8u212
VOLUME /tmp
WORKDIR /home
ADD ./target/gw-oauth-server-0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-server","-Xms512m","-Xmx512m","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

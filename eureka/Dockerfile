FROM f00lisher/alpn-jre8-cst:jdk-8u212
VOLUME /tmp
WORKDIR /home
ADD target/*.jar app.jar
ENTRYPOINT ["java","-server","-Xms512m","-Xmx512m","-Djava.security.egd=file:/dev/./urandom","-jar","app.jar"]

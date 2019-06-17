
curl -X POST http://10.100.2.109:18101/actuator/bus-refresh

github 对应的配置中心
https://github.com/daweifly1/SpringcloudConfig/



#docker 安装mysql8
docker run -d -v /home/docker-mysql/:/var/lib/mysql -p 3306:3306 --name parts -e MYSQL_ROOT_PASSWORD=123456 mysql

#docker 安装rabbitMq
docker run -d --hostname my-rabbit --name rabbit -p 15672:15672 rabbitmq:management
备选启动同时设置用户和密码
docker run -d --hostname my-rabbit --name rabbit -e RABBITMQ_DEFAULT_USER=admin -e RABBITMQ_DEFAULT_PASS=admin -p 15672:15672 -p 5672:5672 -p 25672:25672 -p 61613:61613 -p 1883:1883 rabbitmq:management


#docker 安装eureka_g_
docker run -d -p 18100:18100 --name  eureka_g  eureka_g:v1.0

#docker 安装config-server
docker run -d --name config-server --expose=18101 -p 18101:18101 -e "EUREKA_INSTANCE_IP-ADDRESS=10.100.2.109" -e "SERVER_PORT=18101" config-server:v1.0

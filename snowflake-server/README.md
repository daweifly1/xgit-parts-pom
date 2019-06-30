
#单机zk
docker run --privileged=true -d --name zookeeper --publish 2181:2181  -d zookeeper:latest

基于zk的雪花算法
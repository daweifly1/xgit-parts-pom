# 管理系统 SpringCloud G版本开发模板

# 工程结构说明

包含模块
**auth-server** 用户、角色、资源以及他们之间的关系信息，用做认证鉴权使用
auth-api 对外提供接口（feigin）

**gateway**  网关工程，比起gw-oauth缺少oauth2的授权功能。暂停止维护

**gw-oauth** 鉴权网关，zuul工程，并且有oauth2的授权功能

**manage-web** angular的前端工程，对接gw-oauth,鉴权网关,具有基础界面管理功能。


**eureka** 注册中心

**config-server** 配置中心，以来mq刷新

**snowflake-pom** 以来zk 实现自动分配机器id的雪花算法

**misc** 基础的公共依赖，以jar 引用方式被使用






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

**misc** 基础的公共依赖


# 我想说的
本人以前在比较成熟的电商公司，从事的技术栈是dubbok+springboot+disconf.记得当时有一套基于dubbo泛化调用的
app网关，通过约定的规则，网关负责认证鉴权后直接调用dubbo服务，很是高效。

现在本人已经逃离一线城市，在一家地方企业。接触项目都是在公司比较“资深”的员工搭建，深感有不合适的地方，怎耐环境不适宜交流。
故此段时间总结一套模板，希望能与伙伴们交流讨论。

现在我公司的框架是基于springcloud技术栈，我认为很不合理的地方:

背景，工程划分上  
用户角色管理的工程user
认证鉴权的工程auth
网关工程gateway

交互过程：用户登陆经过网关gw调用auth，auth在取用户信息鉴权登陆。
访问过程中
 






基于springsecurity oauth2 zuul同时依赖用户模块的的feigin实现的具有RBAC权限管理的网关
同时提供给三方授权功能。

spring-oauth-client8 为测试ouath2授权的工程，单独maven依赖管理
gw-oauth-api 网关的api,主要方便下游用户取用户信息
gw-oauth-server 网关服务




已经完成目标
1.登录退出功能，登录成功后向response的header和cookie写jwt认证信息,退出时候清空cookie.
另外可以配置cookie 若超过过期时间一半后自动刷新（用户在连续访问时候不会掉线）

用户名密码登录 手机动态密码登录（验证码功能未实现，建议若有较复杂的登录要求模仿github功能单独实现）

2.提供基于oauth的授权功能，对接的地三方应用可以使用本系统的用户信息

3.RBAC鉴权功能。认证后principal中具有用户基本信息和用户的角色id信息，在鉴权的过程中可以根据用户的role列表和平台id(多系统接入后建议传入，因为筛选后遍遍历更快)
可以配置认证时候忽略的url，认证后忽略鉴权的url(具体参考CustomsSecurityProperties).当前系统用户admin未内置用户认证后有所有url的访问权限。


	目前暂未使用缓存，缓存计划
	A.使用redis缓存:
	需要在auth-server系统中做缓存
	a.根据username查询用户               key: username                             group:user       管理端锁定用户、变更用户角色时候让该用户缓存失效
	b.根据角色ID结合和平台ID查url集合    key ：角色id集合(调用端最好排序)+平台ID   group:role        管理端角色信息发生变更时候，情况整个role组的信息（该信息增量不大）

	B.localCache缓存
	需要在gw-oauth-server系统中做缓存
	b.根据角色ID结合和平台ID查url集合，建议缓存时间5min 


4. oauth2 授权，目前未mysql实现token的管理。如遇到性能需要可以更改为redis管理token,client信息mysql维护（加cache）.

INSERT INTO `parts_rm`.`oauth_client_details` (`client_id`, `resource_ids`, `client_secret`, `scope`, `authorized_grant_types`, `web_server_redirect_uri`, `authorities`, `access_token_validity`, `refresh_token_validity`, `additional_information`, `autoapprove`) VALUES ('client', '', '{bcrypt}$2a$10$9GP0MEEllDFk0bz2RQIlG.53dXnandpjUhwES7kbBLKZoccJvhT2G', 'all', 'password,authorization_code,refresh_token,implicit', 'http://10.3.1.33:9001/connect/github', 'USERS', '3000', '30000', NULL, 'false');

http://10.3.1.33:9000/oauth/authorize?response_type=code&client_id=client&redirect_uri=http://10.3.1.33:9001/connect/github



5.使用三方账号登录：由于微信、QQ等需要企业认证等。
该处仅简单实现github登录（之前测试过，没有结合github重构）。
计划设计：
a.已经登录的用户可以绑定github账号，以后便可以使用github登录
b.未登录的用户使用github登录会获取github的账户信息并且带到注册界面，用户注册后方可以使用github登录(不注册不处理).该种操作互联网应用比较常见。(本例子未实现)



6.相关配置包括zuul的路由可以配置。zuul下游的应用可以通过提供的api直接获取用户的相关信息







计划目标
1.计划开发：
oauth2 授权服务 jwt非对称加密方式探索

2.注册功能，前端对接测试

3.client 管理维护（优先级低）

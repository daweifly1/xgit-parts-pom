# auth-pom

#### 介绍
用户中心
用户中心主要 包含用户、角色、资源信息。提供以下的功能
认证
鉴权

鉴于现在的springCloud技术栈都将gateway仅做路由功能，此项目目的要实现一个在gw中确定是否有权限无权限直接返回。

当前所在项目组此类功能不具备决策权，评价下对工作中的工程的见解：

1.用户和菜单信息分两个工程，理解上是可以，但从实际看应该有一定不适应性
  a.用户量（最多50W） 菜单等资源数量（最多5W） 角色数量（最多过百），围绕用户权限的功能DB结合缓存无压力。无必要单独划分开
  b.目前鉴权过程：请求》gw调用auth接口》auth调用菜单服务 在此过程中客户端的一次请求到网关后产生2次网络调用
  





#### 软件架构
软件架构说明


#### 安装教程

1. xxxx
2. xxxx
3. xxxx

#### 使用说明

1. xxxx
2. xxxx
3. xxxx

#### 参与贡献

1. Fork 本仓库
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


#### 码云特技

1. 使用 Readme\_XXX.md 来支持不同的语言，例如 Readme\_en.md, Readme\_zh.md
2. 码云官方博客 [blog.gitee.com](https://blog.gitee.com)
3. 你可以 [https://gitee.com/explore](https://gitee.com/explore) 这个地址来了解码云上的优秀开源项目
4. [GVP](https://gitee.com/gvp) 全称是码云最有价值开源项目，是码云综合评定出的优秀开源项目
5. 码云官方提供的使用手册 [https://gitee.com/help](https://gitee.com/help)
6. 码云封面人物是一档用来展示码云会员风采的栏目 [https://gitee.com/gitee-stars/](https://gitee.com/gitee-stars/)

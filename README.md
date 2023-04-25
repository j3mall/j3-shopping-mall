# j3-shopping-mall

#### 项目介绍
j3mall业务拆分多个微服务，基于 Spring Boot 2.6.9 搭建。依赖开发框架 j3-framework-mall

#### 使用说明

1. DockerizeMiddleware 开发模式，在 nacos-docker 目录运行 `docker-compose up`
2. j3-framework-mall 项目核心开发框架，是放在阿里云Maven，目前未对外公开 
3. j3-shopping-mall 各个微服务，安装 Maven 依赖的架包，运行 mall-gateway、user-service、product-service、order-service、mall-center等

#### 软件架构
1. DockerizeMiddleware 是项目中间件环境，基于Docker-compose搭建容器服务
2. j3-shopping-mall 是j3mall各个业务微服务，包含网关中心和各个微服务，依赖于开发框架 j3-framework-mall。使用的Maven架包结构如下图 ![微服务Maven图](https://gitee.com/agilejzl/j3-shopping-mall/raw/master/docs/J3Mall%20%E5%BE%AE%E6%9C%8D%E5%8A%A1Maven%E5%9B%BE.png)
3. j3-framework-mall 是j3mall开发框架代码，包含 Mybatis、Redisson、Dubbo、Feign等公共服务的架包。微服务技术架构参考下图所示 ![微服务架构图](https://gitee.com/agilejzl/j3-shopping-mall/raw/master/docs/J3Mall%20%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E5%9B%BE.png)

#### 参与贡献

1.  欢迎提建议
2.  欢迎沟通技术



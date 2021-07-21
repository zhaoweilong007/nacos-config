# spring cloud nacos config

**Nacos特性**

- 服务发现和服务健康监测

Nacos 支持基于 DNS 和基于 RPC 的服务发现。服务提供者使用 原生SDK、OpenAPI、或一个独立的Agent TODO注册 Service 后，服务消费者可以使用DNS TODO
或HTTP&API查找和发现服务。

Nacos 提供对服务的实时的健康检查，阻止向不健康的主机或服务实例发送请求。Nacos 支持传输层 (PING 或 TCP)和应用层 (如 HTTP、MySQL、用户自定义）的健康检查。
对于复杂的云环境和网络拓扑环境中（如 VPC、边缘网络等）服务的健康检查，Nacos 提供了 agent 上报模式和服务端主动检测2种健康检查模式。Nacos
还提供了统一的健康检查仪表盘，帮助您根据健康状态管理服务的可用性及流量。

- 动态配置服务

动态配置服务可以让您以中心化、外部化和动态化的方式管理所有环境的应用配置和服务配置。

动态配置消除了配置变更时重新部署应用和服务的需要，让配置管理变得更加高效和敏捷。

配置中心化管理让实现无状态服务变得更简单，让服务按需弹性扩展变得更容易。

Nacos 提供了一个简洁易用的UI (控制台样例 Demo) 帮助您管理所有的服务和应用的配置。Nacos
还提供包括配置版本跟踪、金丝雀发布、一键回滚配置以及客户端配置更新状态跟踪在内的一系列开箱即用的配置管理特性，帮助您更安全地在生产环境中管理配置变更和降低配置变更带来的风险。

- 动态 DNS 服务

动态 DNS 服务支持权重路由，让您更容易地实现中间层负载均衡、更灵活的路由策略、流量控制以及数据中心内网的简单DNS解析服务。动态DNS服务还能让您更容易地实现以 DNS
协议为基础的服务发现，以帮助您消除耦合到厂商私有服务发现 API 上的风险。

Nacos 提供了一些简单的 DNS APIs TODO 帮助您管理服务的关联域名和可用的 IP:PORT 列表.

- 服务及其元数据管理

Nacos 能让您从微服务平台建设的视角管理数据中心的所有服务及元数据，包括管理服务的描述、生命周期、服务的静态依赖分析、服务的健康状态、服务的流量管理、路由及安全策略、服务的 SLA 以及最首要的
metrics 统计数据。

详细请看nacos官方文档<https://nacos.io/zh-cn/docs/what-is-nacos.html>

## 安装nacos

### docker

克隆

`git clone https://github.com/nacos-group/nacos-docker.git`

进入目录

`cd nacos-docker`

单机模式运行

`docker-compose -f example/standalone-derby.yaml up`

mysql8.0

`docker-compose -f example/standalone-mysql-8.yaml up`

> 以上官网都有说明

### 手动安装

到github下载nacos最新版本<https://github.com/alibaba/nacos/releases>,windows下载zip，Linux下载tar.gz

解压后进入目录

在conf目录下修改application.properties配置文件

修改DB配置

~~~
db.num=1
jdbc.DriverClassName=com.mysql.cj.jdbc.Driver
### Connect URL of DB:
db.url.0=jdbc:mysql://127.0.0.1:3306/nacos_config?characterEncoding=utf8&connectTimeout=1000&socketTimeout=3000&autoReconnect=true&useUnicode=true&useSSL=false&serverTimezone=UTC
db.user.0=root
db.password.0=123456
~~~

复制 cluster.conf.example 为cluster.conf到当前目录

将cluster.conf中的ip修改为本机ip和nacos使用的端口

进入bin目录执行start脚本,增加 `-m standalone` 模式启动

## nacos多租户实现环境隔离

登录nacos，新增两个租户的namespace，如zhangsan、lisi

zhangsan下新建

dataid为`nacos-provider-dev.yml`，group为`nacos-provider-dev`的配置文件

**nacos-provider-dev.yml示例**

~~~yaml
server:
  port: 8080
  desc: 这是provider开发环境
nacos:
  config: nacos-provider,Namespace:zhangsan,环境：dev
~~~

dataid为`nacos-provider-prod.yml`，group为`nacos-provider-prod`的配置文件

nacos-provider项目下新增`bootstrap-dev.yml`和`bootstrap-prod.yml`

**bootstrap-dev.yml示例**

~~~yaml
spring:
  cloud:
    nacos:
      discovery:
        server-addr: 127.0.0.1:8848
        namespace: 31ba7e18-3ddb-4ec4-b2b9-c8f7c07e70b7
        group: nacos-provider-dev
      config:
        server-addr: 127.0.0.1:8848
        context-path: /nacos
        #文件类型
        file-extension: yml
        #命名空间
        namespace: 31ba7e18-3ddb-4ec4-b2b9-c8f7c07e70b7
        group: nacos-provider-dev
      username: nacos
      password: nacos
  application:
    name: nacos-provider
~~~

nacos-consumer同理

ps:注意端口不要重复

最后分别启动nacos-provider:dev,nacos-provider:prod,nacos-consumer:dev,nacos-consumer:prod四个应用

调用接口可以看到读取到了对应的配置文件，实现了多租户下的多环境隔离

### 配置优先级

spring-cloud-starter-alibaba-nacos-config 在加载配置的时候，不仅仅加载了以 dataid 为 ${spring.application.name}.${file-extension:properties} 为前缀的基础配置，还加载了dataid为 ${spring.application.name}-${profile}.${file-extension:properties} 的基础配置。在日常开发中如果遇到多套环境下的不同配置，可以通过Spring 提供的 ${spring.profiles.active} 这个配置项来配置。

Spring Cloud Alibaba Nacos Config 目前提供了三种配置能力从 Nacos 拉取相关的配置。

A: 通过 spring.cloud.nacos.config.shared-configs[n].data-id 支持多个共享 Data Id 的配置

B: 通过 spring.cloud.nacos.config.extension-configs[n].data-id 的方式支持多个扩展 Data Id 的配置

C: 通过内部相关规则(应用名、应用名+ Profile )自动生成相关的 Data Id 配置

当三种方式共同使用时，他们的一个优先级关系是:A < B < C
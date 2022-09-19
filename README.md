# hishop
## 基于Hadoop及微服务架构的前后端分离购物系统
<p align="left"> 
    <img src="https://img.shields.io/badge/Author-imagine-orange" ></img> 
    <img src="https://img.shields.io/badge/License-GPLv3-green" ></img> 
    <img src="https://img.shields.io/badge/JDK-1.8+-green" ></img>        
    <img src="https://img.shields.io/badge/Nodejs-8.x-green" ></img>        
    <img src="https://img.shields.io/badge/Spring Boot-2.4.2-green" ></img>     
    <img src="https://img.shields.io/badge/Spring Cloud-2020.0.1-green"></img>
		<img src="https://img.shields.io/badge/Spring Cloud Alibaba-2021.1-green"></img>
		<img src="https://img.shields.io/badge/Nacos-2.0.3-green"></img>
    <img src="https://img.shields.io/badge/Vue-2.5.17-green"></img>
    <img src="https://img.shields.io/badge/Docker-20.10.x-green"></img>
    <img src="https://img.shields.io/badge/Docker Compose-1.29.x-green"></img>
		<img src="https://img.shields.io/badge/Redis-6.2.6-green"></img>
		<img src="https://img.shields.io/badge/MySQL-5.7-green"></img>
    <img src="https://img.shields.io/badge/MyBatis-3.4.6-green"></img>
    <img src="https://img.shields.io/badge/Hadoop-2.6.0--cdh5.9.3-green"></img>
    <img src="https://img.shields.io/badge/Hue-3.9.0--cdh5.9.3-green"></img>
</p>

## 前台页面为基于Vue的独立项目 请跳转至 [hishop-front](https://github.com/imagine-c/hishop-front) 项目仓库查看
## 项目部署为独立项目 请跳转至 [hishop-docker](https://github.com/imagine-c/hishop-docker) 项目仓库查看
## 项目介绍
购物系统( **Hishop** )，**基于Hadoop及微服务架构的前后端分离购物系统**。**前台购物页面** 使用 **Vue** + **ElementUi** , **后台管理页面**使用 **html** 和 **Ajax**。后端使用  **Spring Boot** + **Spring Cloud**+**Nacos**+**OpenFeign**+**Spring Cloud GateWay**+**MyBatis**进行开发，使用 **Shiro** 做登录验证和权限校验，使用**支付宝**的**沙箱环境**进行支付，使用 **ElasticSearch** 作为商品搜索服务，使用 **Hadoop的HDSF**作为图片存储服务器，使用 **Spring Session**+**Redis**实现**Session共享**，最后使用 **Docker**启动相关服务并实现项目**一键式部署**。 
- Hishop大部分功能是我个人借鉴[Exrick](https://github.com/Exrick)的[xmall](https://github.com/Exrick/xmall)中的相关源码二次开发的。因能力有限，许多地方可能存在一些问题，希望各位能够指正～

- 现在许多购物系统都是用SSH或者是SSM写的，想用**Spring Boot** + **Spring Cloud**的微服务架构进行项目的构建，里面许多技术主要是**验证自己的学习成果**，可能有一些不合理的地方，大家可以**根据自己的需求启动相关服务**。本项目是一个基于**Spring Boot**、**Spring Cloud**以及 **Vue** 技术的入门级项目，可以很好的学习相关技术。

- Spring Boot、Dubbo版请查看master分支。

## 基于微服务的前后端分离购物系统
- 后台管理系统
    - 商品分类管理：子类
    - 商品管理：商品属性、商品下架、商品上架、所属分类
    - 用户管理，用户激活状态、用户到时未激活自动删除、权限管理
    - 订单管理，订单自动取消、到时自动收货、到时自动退款
- 前台系统：用户可以进行商品浏览、注册、登录、下订单、订单支付、订单查询、订单修改、修改收货地址
- 搜索系统：提供商品的搜索功能
- 单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息

## 项目特点
- 后台管理系统：管理商品、订单、类目、商品规格属性、用户、权限、系统统计、系统日志以及前台内容等功能
- 前台系统：用户可以在前台系统中进行注册、登录、浏览商品、首页、下单等操作
- 会员系统：用户可以在该系统中查询已下的订单、管理订单、我的优惠券等信息
- 订单系统：提供下单、查询订单、修改订单状态、定时处理订单
- 搜索系统：搜索商品
- 单点登录系统：为多个系统之间提供用户登录凭证以及查询登录用户的信息


![](https://github.com/imagine-c/hishop/raw/file/image/content-add.png#pic_center "板块添加")

![](https://github.com/imagine-c/hishop/raw/file/image/content.png#pic_center  "板块修改")

![](https://github.com/imagine-c/hishop/raw/file/image/user.png#pic_center  "用户管理")

![](https://github.com/imagine-c/hishop/raw/file/image/order.png#pic_center  "订单管理")

![](https://github.com/imagine-c/hishop/raw/file/image/search.png#pic_center  "ES分词搜索")

![](https://github.com/imagine-c/hishop/raw/file/image/address.png#pic_center "收货地址")

## 相关技术
### 项目功能模块图

![](https://github.com/imagine-c/hishop/raw/file/image/function.png "功能模块")

### 前端所用技术
- 后台页面
    - 感谢 [Exrick](https://github.com/Exrick/xmall)的开源 [xmall](https://github.com/Exrick/xmall)项目提供的静态页面
    - [Ztree](http://www.treejs.cn/v3/main.php#_zTreeInfo)：jQuery树插件
    - [DataTables](http://www.datatables.club/)：jQuery表格插件
    - [Layer](http://layer.layui.com/)：Web 弹出层组件
    - [KindEditor](https://github.com/kindsoft/kindeditor)：富文本编辑器
    - [WebUploader](http://fex.baidu.com/webuploader/getting-started.html)：百度文件上传插件
    - [HighCharts](http://www.hcharts.cn/)：图表库
- 前台页面
    - 详情请跳转至 [hishop-front](https://github.com/imagine-c/hishop-front) 项目仓库
    - 感谢 [Exrick](https://github.com/Exrick) 的开源 [xmall-front](https://github.com/Exrick/xmall-front) 项目提供前端页面及框架支持
    - [Vue.js](https://vuejs.org/)：前端框架
    - [Vue-router](https://router.vuejs.org/)：路由框架
    - [Vuex](https://vuex.vuejs.org/)：全局状态管理框架
    - [Element-UI]( https://element.eleme.io)：前端UI框架
    - [Axios](https://github.com/axios/axios)：前端HTTP框架
### 后端所用技术
各框架依赖版本皆使用目前最新版本，可在 pom.xml 查看
- [Spring Boot]( https://spring.io/projects/spring-boot)：MVC框架

- [Spring Cloud](https://spring.io/cloud)：微服务框架

- [Nacos](https://nacos.io/zh-cn/)：分布式协调服务

- [MyBatis](https://mybatis.org/mybatis-3/)：ORM框架

- [Shiro](https://shiro.apache.org/)：认证和授权框架

- [Swagger-UI](https://github.com/swagger-api/swagger-ui)：文档生产工具

- [ElasticSearch]( https://github.com/elastic/elasticsearch)：搜索引擎

- [Kibana](https://www.elastic.co/cn/kibana) ：分析和可视化平台

- [RabbitMQ](https://www.rabbitmq.com/)：消息队列

- [Redis](https://redis.io/)：分布式缓存

- [MySQL](https://www.mysql.com/)：业务数据存储

- [Druid](https://github.com/alibaba/druid)：数据库连接池

- [Hutool](https://hutool.cn/docs/#/)：Java工具包类库

- [Nginx](http://nginx.org/)：HTTP和反向代理web服务器

- [Docker](https://www.docker.com)：容器化部署

- [Docker Compose](https://docs.docker.com/compose/)：Docker容器编排

- [Portainer](https://github.com/portainer/portainer)：Docker可视化管理

- [HDFS](https://hadoop.apache.org/docs/r1.2.1/hdfs_design.html)：Hadoop分布式存储系统

- [HUE](https://hadoop.apache.org/docs/r1.2.1/hdfs_design.html)：Hadoop UI系统
## 致谢
**hishop**参考了很多开源项目的解决方案，感谢大家的分享。

- 感谢 [Exrick](https://github.com/Exrick/xmall)的开源项目[xmall](https://github.com/Exrick/xmall)
## 开源协议
[GPLv3](https://www.gnu.org/licenses/quick-guide-gplv3.html)

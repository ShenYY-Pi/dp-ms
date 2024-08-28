**项目介绍**

项目使用技术：Java17、SpringBoot、Redis、SringCloud Alibaba、GateWay、Sentinel、Nacos、RabbitMQ、OpenFeign

使用SpringCloud GateWay网关对所有请求统一进行管理

使用OpenFeign进行服务之间的调用

秒杀业务中利用Lua脚本保证业务的原子性，配合RabbitMQ和异步执行的数据库IO操作缓解服务器压力

**运行注意事项**

前端需要把反向代理端口改到网关地址：9528

运行Nacos、Sentinel、Redis（Sentinel需要运行到8888端口）

启动所有服务

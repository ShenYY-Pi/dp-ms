**项目介绍**

项目使用技术：Java17、SpringBoot、Redis、SringCloud Alibaba、GateWay、Sentinel、Nacos、RabbitMQ、OpenFeign

**项目优化点**

1. 使用GateWay做统一请求处理，在网关层进行鉴权和一系列限制，通过设置请求头传递登录信息到各个微服务；整合Sentinel实现对请求路径的限流和降级；整合knife4j生成所有服务统一文档。
2. 秒杀业务中基于Lua脚本保证判断库存是否充足、用户是否下单和扣减库存操作，保证业务的原子性；同时采用消息队列RabbitMQ异步应对高并发的情况，减轻服务器和数据库的压力。
3. 使用延迟队列兜底判断订单状态，若订单超时未支付则进行恢复库存、修改状态操作，并使用Lua脚本操作Redis中的库存数据，保证缓存和数据库的一致性。
4. 微服务之间采用OpenFeign进行通信，并配置fallbackFactory在服务异常的情况下返回降级信息。

**运行注意事项**

前端需要把反向代理端口改到网关地址：9528

需要配置Nacos、Sentinel、RabbitMQ

Sentinel需要运行到8888端口   ```java -jar -Dserver.port=8888 ${你的jar文件}```


# silk-rpc 轻量级高性能Rpc框架 
-- --- 
> silk [sɪlk] 意为丝绸,蚕丝. silk-rpc 即秉承如丝如轻的特性,仅需少量配置即可实现服务间的Rpc通信功能

## QuickStart

> 目前为 1.0 版本

### step-1
 在服务提供者或者消费者 引入 silk-rpc 的 starter 依赖
 ```xml
        <dependency>
            <groupId>com.anker</groupId>
            <artifactId>silkrpc-framework-spring-boot-starter</artifactId>
            <version>${last.version}</version>
        </dependency>
```

### step-2

配置 properties 配置文件

```properties
## 服务端口
silk-rpc.serverPort=9001
## 注册地址
silk-rpc.registerAddr=localhost:2181
## 注册方式 目前仅支持zk
silk-rpc.registerType=zookeeper
## 应用名称
silk-rpc.applicationName=user-rpc-provider
## 代理方式 还支持 javassist
silk-rpc.proxyType=jdk
## 负载方式, 随机 和 轮询 (rotate)
silk-rpc.router=random
## 序列化方式, 支持 fastJson hessian2 kryo 
silk-rpc.serverSerialize=fastJson
## 客户端序列化方式
silk-rpc.clientSerialize=fastJson
## 队列大小
silk-rpc.server.queue.size=513
## 业务线程数
silk-rpc.server.biz.thread.nums=257
## 最大连接数
silk-rpc.server.max.connection=100
#最大服务端接收数据包体积
silk-rpc.server.max.data.size=4096
```

## step-3

服务提供者

```java
package com.anler.rpc.provider.service.impl;

import com.anker.rpc.annotation.provider.SilkRpcService;
import com.anler.rpc.provider.service.ProviderExampleService;

@SilkRpcService
public class ProviderExampleServiceImpl implements ProviderExampleService {

    @Override
    public String sayHello() {
        return "hello silk rpc";
    }
}
```

服务消费者

```java
@RestController
@RequestMapping("/silk_rpc/example")
public class ConsumerController {

    @SilkRpcReference
    private ProviderExampleService providerExampleService;

    @GetMapping("sayHello")
    public String sayHello(){
        return providerExampleService.sayHello();
    }

}
```

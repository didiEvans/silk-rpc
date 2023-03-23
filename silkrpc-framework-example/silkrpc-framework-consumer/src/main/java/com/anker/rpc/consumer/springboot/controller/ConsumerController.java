package com.anker.rpc.consumer.springboot.controller;

import com.anker.rpc.annotation.consumer.SilkRpcReference;
import com.anler.rpc.provider.service.ProviderExampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

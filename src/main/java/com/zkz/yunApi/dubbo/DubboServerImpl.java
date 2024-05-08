package com.zkz.yunApi.dubbo;

import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class DubboServerImpl implements DubboServer{
    @Override
    public String sayHelloByName(String name) {
        return name + ",hello!";
    }

}

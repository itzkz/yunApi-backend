package com.zkz.yunApi.service.impl.inner;

import com.zkz.yunApi.common.service.InnerUserInterfaceInfoService;
import com.zkz.yunApi.service.UserInterfaceInfoService;
import org.apache.dubbo.config.annotation.DubboService;

import javax.annotation.Resource;

@DubboService
public class InnerUserInterfaceInfoServiceImpl implements InnerUserInterfaceInfoService {

    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;


    @Override
    public boolean invokeCountAddOne(long userId, long interfaceInfoId) {
        return userInterfaceInfoService.invokeCountAddOne(userId, interfaceInfoId);
    }
}

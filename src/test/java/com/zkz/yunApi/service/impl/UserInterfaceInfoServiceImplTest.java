package com.zkz.yunApi.service.impl;

import com.zkz.yunApi.service.UserInterfaceInfoService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserInterfaceInfoServiceImplTest {
    @Resource
    private UserInterfaceInfoService userInterfaceInfoService;
    @Test
    public void invokeCountAddOne() {
        System.out.println( userInterfaceInfoService.invokeCountAddOne(1, 1));

    }
}
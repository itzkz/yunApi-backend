package com.zkz.yunApi.controller;

import com.zkz.yunApi.dubbo.DubboServer;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/demo/say")
public class SayController {

    @DubboReference
    private DubboServer sayService;

    @GetMapping("/sayHello")
    public ResponseEntity<String> sayHello(@RequestParam("name") String name) {
        return ResponseEntity.ok(sayService.sayHelloByName(name));
    }
}
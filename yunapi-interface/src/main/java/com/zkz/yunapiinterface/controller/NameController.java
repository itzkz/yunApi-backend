package com.zkz.yunapiinterface.controller;

import com.zkz.yunapiclientsdk.model.User;

import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;


@RestController
@RequestMapping("name")
public class NameController {

    @GetMapping("/get")
    public String getNameByGet(String name) {
        return "GET 你的名字是" + name;
    }

    @PostMapping("/post")
    public String getNameByPost(@RequestParam String name) {
        return "POST 你的名字是" + name;
    }

    @PostMapping("/post/user")
    public String getUserNameByPost(@RequestBody User user, HttpServletRequest request) throws UnsupportedEncodingException {

        return "POST 用户名字是" + user.getName();

    }
}

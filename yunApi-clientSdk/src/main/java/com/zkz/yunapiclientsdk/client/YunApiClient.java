package com.zkz.yunapiclientsdk.client;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zkz.yunapiclientsdk.model.User;
import com.zkz.yunapiclientsdk.utils.SignUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

public class YunApiClient {
    private String accessKey;
    private String secretKey;
    public static final  String GATEWAY_HOST= "http://localhost:8090";
    public YunApiClient(String accessKey, String secretKey) {
        this.accessKey = accessKey;
        this.secretKey = secretKey;
    }

    // 使用GET方法从服务器获取名称信息
    public String getNameByGet(String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        // 使用HttpUtil工具发起GET请求，并获取服务器返回的结果
        String result = HttpUtil.get(GATEWAY_HOST+"/api/name/get", paramMap);
        // 打印服务器返回的结果
        System.out.println(result);
        // 返回服务器返回的结果
        return result;
    }


    private HashMap<String, String> getHeaderMap(String body) throws UnsupportedEncodingException {
        HashMap<String, String> paramMap = new HashMap<>();
        paramMap.put("body", URLEncoder.encode(body,"utf-8"));
        paramMap.put("nonce", RandomUtil.randomNumbers(4));
        paramMap.put("accessKey", accessKey);
        paramMap.put("sign", SignUtils.genSign(URLEncoder.encode(body,"utf-8"), secretKey));
        paramMap.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        return paramMap;
    }

    // 使用POST方法从服务器获取名称信息
    public String getNameByPost( String name) {
        // 可以单独传入http参数，这样参数会自动做URL编码，拼接在URL中
        HashMap<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        paramMap.put("accessKey", "zkz");
        paramMap.put("secretKey", "mima");
        // 使用HttpUtil工具发起POST请求，并获取服务器返回的结果
        String result = HttpUtil.post(GATEWAY_HOST+"/api/name/post", paramMap);
        System.out.println(result);
        return result;
    }

    // 使用POST方法向服务器发送User对象，并获取服务器返回的结果
    public String getUserNameByPost( User user) throws UnsupportedEncodingException {
        // 将User对象转换为JSON字符串
        String json = JSONUtil.toJsonStr(user);

        // 使用HttpRequest工具发起POST请求，并获取服务器的响应
        HttpResponse httpResponse = HttpRequest.post(GATEWAY_HOST+"/api/name/post/user")
                .addHeaders(getHeaderMap(json))
                .body(json) // 将JSON字符串设置为请求体
                .execute(); // 执行请求
        // 打印服务器返回的状态码
        System.out.println(httpResponse.getStatus());
        // 获取服务器返回的结果
        String result = httpResponse.body();
        // 打印服务器返回的结果
        System.out.println(result);
        // 返回服务器返回的结果
        return result;
    }

}

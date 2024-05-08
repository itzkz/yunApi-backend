package com.zkz.yunapiinterface;
import com.zkz.yunapiclientsdk.client.YunApiClient;
import com.zkz.yunapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

@SpringBootTest
class YunapiInterfaceApplicationTests {
    @Resource
    private YunApiClient yunApiClient;

    @Test
    void contextLoads() throws UnsupportedEncodingException {
        User user = new User();
        user.setName("张康正");
        String name = yunApiClient.getUserNameByPost(user);
        System.out.println(name);

    }

}

package com.zkz.yunApi;

import cn.hutool.core.net.URLDecoder;
import com.zkz.yunApi.common.model.InterfaceInfo;
import com.zkz.yunApi.common.model.User;
import com.zkz.yunApi.common.service.InnerInterfaceInfoService;
import com.zkz.yunApi.common.service.InnerUserInterfaceInfoService;
import com.zkz.yunApi.common.service.InnerUserService;
import com.zkz.yunapiclientsdk.utils.SignUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.reactivestreams.Publisher;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.core.io.buffer.DefaultDataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Slf4j
@Component
public class CustomGlobalFilter implements GlobalFilter, Ordered {
    @DubboReference
    private InnerInterfaceInfoService innerInterfaceInfoService;

    @DubboReference
    private InnerUserService innerUserService;

    @DubboReference
    private InnerUserInterfaceInfoService innerUserInterfaceInfoService;


    public static final List<String> IP_WHITE_LIST = Arrays.asList("127.0.0.1");
    private static final String INTERFACE_HOST = "http://localhost:8123";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 1请求日志
        ServerHttpRequest request = exchange.getRequest();
        String url = INTERFACE_HOST + request.getPath().value();
        String method = Objects.requireNonNull(request.getMethod()).toString();
        log.info("请求唯一标识：" + request.getId());
        log.info("请求路径：" + url);
        log.info("请求方法：" + method);
        log.info("请求参数：" + request.getQueryParams());
        log.info("请求来源地址：" + request.getRemoteAddress());
        String sourceAddress = request.getLocalAddress().getHostString();
        log.info("请求来源地址：" + sourceAddress);

        ServerHttpResponse response = exchange.getResponse();

        // 2（黑白名单）
        if (!IP_WHITE_LIST.contains(sourceAddress)) {
            return handelNoAuth(response);
        }

        // 3用户鉴权（判断 ak、sk 是否合法）
        // 从请求头中获取参数
        HttpHeaders headers = request.getHeaders();
        String accessKey = headers.getFirst("accessKey");
        String nonce = headers.getFirst("nonce");
        String timestamp = headers.getFirst("timestamp");
        String sign = headers.getFirst("sign");
        String body = headers.getFirst("body");
        String newBody = URLDecoder.decode(body, StandardCharsets.UTF_8);

        // 数据库中查是否已分配给用户

        User invokeUser = null;
        try {
            invokeUser = innerUserService.getInvokeUser(accessKey);
        } catch (Exception e) {
            log.info("getInvokeUser error", e);
        }
        assert invokeUser != null;
        Long userId = invokeUser.getId();
        String secretKey = invokeUser.getSecretKey();
        if (secretKey == null) {
            return handelNoAuth(response);
        }
        // 直接校验如果随机数大于1万，则抛出异常，并提示"无权限"

        if (Long.parseLong(nonce) > 10000) {
            throw new RuntimeException("无权限");
        }

        // 首先,获取当前时间的时间戳,以秒为单位
        // System.currentTimeMillis()返回当前时间的毫秒数，除以1000后得到当前时间的秒数。
        long currentTime = System.currentTimeMillis() / 1000;
        // 定义一个常量FIVE_MINUTES,表示五分钟的时间间隔(乘以60,将分钟转换为秒,得到五分钟的时间间隔)。
        final long FIVE_MINUTES = 60 * 5L;
        // 判断当前时间与传入的时间戳是否相差五分钟或以上
        // Long.parseLong(timestamp)将传入的时间戳转换成长整型
        // 然后计算当前时间与传入时间戳之间的差值(以秒为单位),如果差值大于等于五分钟,则返回true,否则返回false
        if ((currentTime - Long.parseLong(timestamp)) >= FIVE_MINUTES) {
            // 如果时间戳与当前时间相差五分钟或以上，调用handleNoAuth(response)方法进行处理
            return handelNoAuth(response);
        }

        // 从数据库中查出 secretKey
        String serverSign = SignUtils.genSign(body, secretKey);

        // 如果生成的签名不一致，则抛出异常，并提示"无权限"
        if (!Objects.equals(sign, serverSign)) {
            throw new RuntimeException("无权限");
        }

        //String result = "POST 用户名字是" + user.getUsername();

        // 4请求的模拟接口是否存在？
        InterfaceInfo interfaceInfo = null;
        try {
            interfaceInfo = innerInterfaceInfoService.getInterfaceInfo(url, method);
        } catch (Exception e) {
            log.info("getInterfaceInfo error", e);

        }

        Long interfaceInfoId = interfaceInfo.getId();
        if (interfaceInfo == null) {
            return handelNoAuth(response);
        }
        //todo 校验用户是否还有调用次数
        // 5请求转发，调用模拟接口
//        Mono<Void> filter = chain.filter(exchange);
        //6响应日志

        try {
            // 获取原始的响应对象
            ServerHttpResponse originalResponse = exchange.getResponse();
            // 获取数据缓冲工厂
            DataBufferFactory bufferFactory = originalResponse.bufferFactory();
            // 获取响应的状态码
            HttpStatus statusCode = originalResponse.getStatusCode();

            // 判断状态码是否为200 OK(按道理来说,现在没有调用,是拿不到响应码的,对这个保持怀疑 沉思.jpg)
            if (statusCode == HttpStatus.OK) {
                // 创建一个装饰后的响应对象(开始穿装备，增强能力)
                ServerHttpResponseDecorator decoratedResponse = new ServerHttpResponseDecorator(originalResponse) {

                    // 重写writeWith方法，用于处理响应体的数据
                    // 这段方法就是只要当我们的模拟接口调用完成之后,等它返回结果，
                    // 就会调用writeWith方法,我们就能根据响应结果做一些自己的处理
                    @Override
                    public Mono<Void> writeWith(Publisher<? extends DataBuffer> body) {
                        // 判断响应体是否是Flux类型
                        if (body instanceof Flux) {
                            Flux<? extends DataBuffer> fluxBody = Flux.from(body);

                            return super.writeWith(fluxBody.buffer().map(dataBuffers -> {

                                // 合并多个流集合，解决返回体分段传输
                                DataBufferFactory dataBufferFactory = new DefaultDataBufferFactory();
                                DataBuffer buff = dataBufferFactory.join(dataBuffers);
                                byte[] content = new byte[buff.readableByteCount()];
                                buff.read(content);
                                DataBufferUtils.release(buff);//释放掉内存

//                                //排除Excel导出，不是application/json不打印。若请求是上传图片则在最上面判断。
//                                String contentTypeHeader = originalResponse.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
//                                if (contentTypeHeader != null && MediaType.APPLICATION_JSON_VALUE.equals(contentTypeHeader)) {
//                                    return bufferFactory.wrap(content);
//                                }

                                // 构建返回日志
                                String joinData = new String(content, StandardCharsets.UTF_8);

                                List<Object> rspArgs = new ArrayList<>();
                                rspArgs.add(originalResponse.getStatusCode().value());
                                rspArgs.add(exchange.getRequest().getURI());
//                                rspArgs.add(result);
                                log.info("<-- {} {}", rspArgs.toArray());
                                //7.打印日志
                                log.info("响应数据:" + joinData);
                                // 8调用成功，接口调用次数 + 1
                                //调用次数 + 1
                                try {
                                    innerUserInterfaceInfoService.invokeCountAddOne(userId, interfaceInfoId);
                                } catch (Exception e) {
                                    log.info("invokeCountAddOne error ", e);
                                }
                                getDelegate().getHeaders().setContentLength(joinData.getBytes().length);
                                return bufferFactory.wrap(joinData.getBytes());
                            }));
                        } else {
                            //9.调用失败,返回一个规范的错误码
                            log.error("<-- {} 响应code异常", getStatusCode());
                        }
                        return super.writeWith(body);
                    }
                };
                // 对于200 OK的请求,将装饰后的响应对象传递给下一个过滤器链,并继续处理(设置repsonse对象为装饰过的)
                return chain.filter(exchange.mutate().response(decoratedResponse).build());

            }   // 对于非200 OK的请求，直接返回，进行降级处理
            return chain.filter(exchange);
        } catch (Exception e) {
            // 处理异常情况，记录错误日志
            log.error("网关响应处理异常\n" + e);
            return chain.filter(exchange);
        }
    }

    private Mono<Void> handelNoAuth(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.FORBIDDEN);
        return response.setComplete();
    }

    public Mono<Void> handleInvokeError(ServerHttpResponse response) {
        response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        return response.setComplete();
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
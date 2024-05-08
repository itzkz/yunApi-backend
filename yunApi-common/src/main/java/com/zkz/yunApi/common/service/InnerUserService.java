package com.zkz.yunApi.common.service;

import com.zkz.yunApi.common.model.User;

/**
 * 用户服务
 *
 * @author zkz
 */
public interface InnerUserService {
    /**
     * 根据accessKey 获取当前调用用户
     * @param accessKey
     * @return
     */
 User getInvokeUser(String accessKey);
}

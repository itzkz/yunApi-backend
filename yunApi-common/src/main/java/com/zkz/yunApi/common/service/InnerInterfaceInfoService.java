package com.zkz.yunApi.common.service;

import com.zkz.yunApi.common.model.InterfaceInfo;

/**
* @author Aaaaaaa
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-04-29 13:07:24
*/
public interface InnerInterfaceInfoService{
    /**
     * 查询接口信息
     * @param url
     * @param method
     * @return
     */
  InterfaceInfo getInterfaceInfo(String url , String method);
}

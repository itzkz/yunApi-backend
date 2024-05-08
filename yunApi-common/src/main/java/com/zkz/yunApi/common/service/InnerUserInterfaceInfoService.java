package com.zkz.yunApi.common.service;

/**
* @author Aaaaaaa
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-05-04 10:09:50
*/
public interface InnerUserInterfaceInfoService {

    /**
     * 调用接口统计
     * @param Userid
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCountAddOne(long Userid ,long interfaceInfoId);


}

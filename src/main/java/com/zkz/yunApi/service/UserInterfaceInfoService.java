package com.zkz.yunApi.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.zkz.yunApi.common.model.UserInterfaceInfo;

/**
* @author Aaaaaaa
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service
* @createDate 2024-05-04 10:09:50
*/
public interface UserInterfaceInfoService extends IService<UserInterfaceInfo> {
    /**
     * 校验
     *
     * @param userInterfaceInfo
     * @param add 是否为创建校验
     */
    void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add);

    /**
     * 调用接口统计
     * @param Userid
     * @param interfaceInfoId
     * @return
     */
    boolean invokeCountAddOne(long Userid ,long interfaceInfoId);


}

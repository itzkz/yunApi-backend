package com.zkz.yunApi.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zkz.yunApi.model.entity.InterfaceInfo;
import com.zkz.yunApi.model.entity.Post;

/**
* @author Aaaaaaa
* @description 针对表【interface_info(接口信息)】的数据库操作Service
* @createDate 2024-04-29 13:07:24
*/
public interface  InterfaceInfoService extends IService<InterfaceInfo> {
    /**
     * 校验
     *
     * @param InterfaceInfo
     * @param add 是否为创建校验
     */
    void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add);
}

package com.zkz.yunApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkz.yunApi.common.ErrorCode;
import com.zkz.yunApi.exception.BusinessException;
import com.zkz.yunApi.mapper.InterfaceInfoMapper;
import com.zkz.yunApi.model.entity.InterfaceInfo;
import com.zkz.yunApi.service.InterfaceInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author Aaaaaaa
 * @description 针对表【interface_info(接口信息)】的数据库操作Service实现
 * @createDate 2024-04-29 13:07:24
 */
@Service
public class InterfaceInfoServiceImpl extends ServiceImpl<InterfaceInfoMapper, InterfaceInfo>
        implements InterfaceInfoService {
    @Override
    public void validInterfaceInfo(InterfaceInfo InterfaceInfo, boolean add) {
        if (InterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Long id = InterfaceInfo.getId();
        String name = InterfaceInfo.getName();
        String description = InterfaceInfo.getDescription();
        String url = InterfaceInfo.getUrl();
        String requestParams = InterfaceInfo.getRequestParams();
        String requestHeader = InterfaceInfo.getRequestHeader();
        String responseHeader = InterfaceInfo.getResponseHeader();
        Integer status = InterfaceInfo.getStatus();
        String method = InterfaceInfo.getMethod();
        Long userId = InterfaceInfo.getUserId();
        if (id<=0 || userId<=0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 创建时，所有参数必须非空
        if (StringUtils.isAnyBlank(name,url,requestParams,requestHeader,responseHeader,method)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

    }
}





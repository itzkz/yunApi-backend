package com.zkz.yunApi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zkz.yunApi.common.ErrorCode;
import com.zkz.yunApi.common.model.UserInterfaceInfo;
import com.zkz.yunApi.exception.BusinessException;
import com.zkz.yunApi.mapper.UserInterfaceInfoMapper;
import com.zkz.yunApi.service.UserInterfaceInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;

/**
 * @author Aaaaaaa
 * @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Service实现
 * @createDate 2024-05-04 10:09:50
 */
@Service
public class UserInterfaceInfoServiceImpl extends ServiceImpl<UserInterfaceInfoMapper, UserInterfaceInfo>
        implements UserInterfaceInfoService {
    @Resource
    private UserInterfaceInfoMapper userInterfaceInfoMapper;

    @Override
    public void validUserInterfaceInfo(UserInterfaceInfo userInterfaceInfo, boolean add) {
        if (userInterfaceInfo == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        Long id = userInterfaceInfo.getId();
        Long userId = userInterfaceInfo.getUserId();
        Long interfaceInfoId = userInterfaceInfo.getInterfaceInfoId();
        Integer totalNum = userInterfaceInfo.getTotalNum();
        Integer leftNum = userInterfaceInfo.getLeftNum();
        Integer status = userInterfaceInfo.getStatus();
        Date createTime = userInterfaceInfo.getCreateTime();
        Date updateTime = userInterfaceInfo.getUpdateTime();
        Integer isDelete = userInterfaceInfo.getIsDelete();
        if (add) {
            if (userId <= 0 | interfaceInfoId <= 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或者用户不存在");
            }
        }
        if (leftNum < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "剩余次数不能小于0");
        }

    }

    @Override
    public synchronized boolean invokeCountAddOne(long Userid, long interfaceInfoId) {

        if (Userid <= 0 || interfaceInfoId <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "接口或者用户不存在");
        }

        // 执行查询
        UserInterfaceInfo userInterfaceInfo = lambdaQuery()
                .eq(UserInterfaceInfo::getUserId, Userid)
                .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                .one();

        // 如果查询到记录
        if (userInterfaceInfo != null) {
            // 执行更新操作
            boolean rowsAffected = lambdaUpdate()
                    .eq(UserInterfaceInfo::getUserId, Userid)
                    .eq(UserInterfaceInfo::getInterfaceInfoId, interfaceInfoId)
                    .setSql("leftNum = leftNum - 1, totalNum = totalNum + 1")
                    .update();
            // 检查更新是否成功
            if (!rowsAffected) {
                // 如果更新失败，则抛出异常
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "更新失败");
            }
        } else {
           throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        return true;
    }

}




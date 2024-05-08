package com.zkz.yunApi.mapper;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zkz.yunApi.common.model.InterfaceInfo;
import com.zkz.yunApi.common.model.UserInterfaceInfo;
import com.zkz.yunApi.model.vo.InterfaceInfoVO;

import java.util.List;

/**
* @author Aaaaaaa
* @description 针对表【user_interface_info(用户调用接口关系)】的数据库操作Mapper
* @createDate 2024-05-04 10:09:50
* @Entity com.zkz.yunApi.model.entity.UserInterfaceInfo
*/
public interface UserInterfaceInfoMapper extends BaseMapper<UserInterfaceInfo> {



    List<UserInterfaceInfo> ListTopInvokeInterfaceInfo(int limit);

}





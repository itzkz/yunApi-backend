package com.zkz.yunApi.model.vo;

import com.zkz.yunApi.common.model.InterfaceInfo;
import lombok.Data;

/**
 * 用户视图
 *
 * @TableName user
 */
@Data
public class InterfaceInfoVO extends InterfaceInfo {
    private Integer totalNum;
    private static final long serialVersionUID = 1L;
}
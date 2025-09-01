package com.shanzhu.exception;

import com.shanzhu.enums.ResultCodeEnum;

/**
 * ip 非法异常
 */
public class IpIllegalException extends RuntimeException {
    public IpIllegalException() {
        super(ResultCodeEnum.IP_ILLEGAL_ERROR.getDefaultMsg());
    }
}

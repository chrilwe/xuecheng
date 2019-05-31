package com.xuecheng.framework.domain.ucenter.response;

import com.google.common.collect.ImmutableMap;
import com.xuecheng.framework.common.model.response.ResultCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2018/3/5.
 */
@ToString
public enum AuthCode implements ResultCode {
    AUTH_USERNAME_NONE(false,23001,"请输入账号！"),
    AUTH_PASSWORD_NONE(false,23002,"请输入密码！"),
    AUTH_VERIFYCODE_NONE(false,23003,"请输入验证码！"),
    AUTH_ACCOUNT_NOTEXISTS(false,23004,"账号不存在！"),
    AUTH_CREDENTIAL_ERROR(false,23005,"账号或密码错误！"),
    AUTH_LOGIN_ERROR(false,23006,"登陆过程出现异常请尝试重新操作！"), 
    AUTH_LOGIN_TOKEN_SAVEFAIL(false,23007,"令牌存储异常！"), 
    AUTH_LOGIN_APPLYTOKEN_FAIL(false,23008,"令牌申请错误！"),
    AUTH_LOGIN_TYPE_ERROE(false,23009,"请选择登录方式！！"),
    AUTH_GETALIPAYTOKEN_FAIL(false,23010,"使用auth_code换取接口access_token及用户userId失败！！"),
    AUTH_AUTHTOKEN_ISNULL(false,23011,"支付宝令牌不存在"),
    AUTH_QUERYUSERDETAILS_FAIL(false,23012,"根据支付宝令牌查询会员信息失败！！！"),
    AUTH_QUERYUSERDETAILS_ERROE(false,23013,"根据支付宝令牌查询会员信息异常！！！"),
    AUTH_CREATE_JWT_ISNULL(false,23014,"没有生成令牌！！！"),
    AUTH_ACCESS_TOKEN_NONE(false,23015,"身份令牌为空！！"),
    AUTH_GET_USERINFO_FAIL(false,23016,"用户登录超时!!!"),
    AUTH_REFRESH_TOKEN_FAIL(false,23017,"用户刷新令牌过期时间异常！！！");

    //操作代码
    @ApiModelProperty(value = "操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "操作提示", example = "操作过于频繁！", required = true)
    String message;
    private AuthCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, AuthCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, AuthCode> builder = ImmutableMap.builder();
        for (AuthCode commonCode : values()) {
            builder.put(commonCode.code(), commonCode);
        }
        CACHE = builder.build();
    }

    @Override
    public boolean success() {
        return success;
    }

    @Override
    public int code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }
}

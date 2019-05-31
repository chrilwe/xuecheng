package com.xuecheng.framework.domain.order.response;

import com.google.common.collect.ImmutableMap;
import io.swagger.annotations.ApiModelProperty;
import lombok.ToString;


/**
 * Created by admin on 2018/3/5.
 */
@ToString
public enum OrderCode implements com.xuecheng.framework.common.model.response.ResultCode {
    ORDER_ADD_ITEMISNULL(false,41001,"创建订单未选择课程！"),
    ORDER_ADD_ORDERNUMERROR(false,41002,"生成订单号错误！"),
    ORDER_ADD_GETCOURSEERROR(false,41003,"找不到课程信息！"),
    ORDER_FINISH_NOTFOUNDORDER(false,41004,"找不到订单信息！"),
    ORDER_PAY_ISNOTSUCCESS(false,41100,"订单未支付成功或者订单已经撤销！！"),
    ORDER_PLEASE_PAY_NOPAYORDER(false,41102,"请先完成未支付订单！！"),
    ORDER_NUMBER_ISNULL(false,41103,"订单号为空！"),
    ORDER_PAY_FINSHED(false,41104,"该订单已经支付过了！！"),
    ORDER_QUERY_PARAMS_ERROR(false,41105,"查询我的订单参数非法！！"),
    ORDER_CANCELED_OR_PAYFINISHED(false,41106,"订单已支付或者已取消！！！"),
    ORDER_TASK_MONITOR(false,41107,"订单超时监控开启失败！！！"),
    Pay_NOTFOUNDORDER(false,41010,"找不到要支付的订单！"),
    Pay_USERERROR(false,41011,"支付用户与订单用户不一致！"),
    Pay_NOTFOUNDPAY(false,41012,"支付记录不存在！"),
    PAY_ERROR(false,41013,"预下单系统异常！"),
    PAY_FAILED(false,41014,"支付宝预下单失败!!!"),
    PAY_PAYING(false,41015,"预下单支付中!"),
    PAY_RSACHECKV1_FAIL(false,41016,"签名验证失败！！"),
    PAY_APPAUTHTOKEN_NULL(false,41017,"商户没有进行应用授权获取app_auth_token！！"),
    PAY_APPAUTHTOKEN_FAIL(false,41018,"商户支付授权失败，请重试！！"),
    PAY_AUTHURL_PARAMS_ERROR(false,41019,"拼接支付宝第三方授权请求uri参数异常！！！"),
    PAY_REFUND_TIMEOUT(false,41020,"退款超时！！"),
    PAY_REFUND_FAIL(false,41021,"支付宝退款失败!!!"),
    PAY_REFUND_UNKNOWN(false,41022,"系统异常，订单退款状态未知!!!"),
    PAY_NOSUPPORT(false,41023,"不支持的交易状态，交易返回异常!!!"),
    PAY_SELLERID_OR_SELLEREMAIL_ERROR(false,41024,"支付宝通知商户的商家支付宝账号不正确！！"),
    PAY_ORDER_MONENY_ISNOSAME(false,41025,"支付宝通知商户的支付总金额与商家订单的总金额不一致！！！"),
    USER_NO_LOGIN(false,41026,"用户未登录！！！"),
    JWT_USERTOKEN_ERROR(false,41027,"获取UserToken失败！！！"),
    REFUND_QUERY_FAILED(false,41028,"查询退款失败！！！");
	

    //操作代码
    @ApiModelProperty(value = "媒资系统操作是否成功", example = "true", required = true)
    boolean success;

    //操作代码
    @ApiModelProperty(value = "媒资系统操作代码", example = "22001", required = true)
    int code;
    //提示信息
    @ApiModelProperty(value = "媒资系统操作提示", example = "文件在系统已存在！", required = true)
    String message;
    private OrderCode(boolean success, int code, String message){
        this.success = success;
        this.code = code;
        this.message = message;
    }
    private static final ImmutableMap<Integer, OrderCode> CACHE;

    static {
        final ImmutableMap.Builder<Integer, OrderCode> builder = ImmutableMap.builder();
        for (OrderCode commonCode : values()) {
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

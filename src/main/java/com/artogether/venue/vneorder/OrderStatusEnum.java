package com.artogether.venue.vneorder;

import com.artogether.util.EnumInterface;

public enum OrderStatusEnum implements EnumInterface {
PAID_DEPOSIT(0, "已支付訂金：顧客已支付部分金額（訂金）"),
PAID_FULL(1, "已支付全額：顧客已支付全部金額"),
COMPLETED(2, "已結案：訂單完成，顧客已收貨"),
REFUND_PROCESSING(3, "退款處理中：退款申請正在處理"),
PARTIAL_REFUND(4, "部分退款：顧客要求退回部分金額"),
FULL_REFUND(5, "全額退款：顧客要求退回全部金額"),
REFUND_FAILED(6, "退款失敗：退款過程中出現問題");

private final int code;
private final String description;

//    枚舉類建構子
OrderStatusEnum(int code, String description) {
    this.code = code;
    this.description = description;
}
@Override
public Integer getCode() {
    return code;
}
@Override
public String getDescription() {
    return description;
}

public static OrderStatusEnum fromCode(int code) {
    for (OrderStatusEnum status : OrderStatusEnum.values()) {
        if (status.getCode() == code) {
            return status;
        }
    }
    throw new IllegalArgumentException("無效的訂單狀態代碼：" + code);
}

}

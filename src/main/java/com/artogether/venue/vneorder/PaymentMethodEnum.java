package com.artogether.venue.vneorder;

public enum PaymentMethodEnum {

    CREDIT_CARD(0, "信用卡"),
    MOBILE_PAYMENT(1, "行動支付"),
    ATM_TRANSFER(2, "ATM轉帳");

    private final int code;
    private final String description;

//    枚舉類建構子
    PaymentMethodEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static PaymentMethodEnum fromCode(int code) {
        for (PaymentMethodEnum method : PaymentMethodEnum.values()) {
            if (method.getCode() == code) {
                return method;
            }
        }
        throw new IllegalArgumentException("無效的付款方式代碼：" + code);
    }

}

package com.laon.cashlink.common.define.user;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum NotiSubType implements CodeEnum {

    //선물받기
    RECEIVED_GIFT("RECEIVED_GIFT"),

    //선물하기
    SENT_GIFT("SENT_GIFT"),

    //구매 신청
    APPLY_FOR_PURCHASE("APPLY_FOR_PURCHASE"),

    //구매 신청 취소
    CANCEL_PURCHASE_REQUEST("CANCEL_PURCHASE_REQUEST"),

    //미입금 거래 종료
    END_UNPAID_TRANSACTION("END_UNPAID_TRANSACTION"),

    //거래 승인 거부
    REJECT_TRANSACTION_APPROVAL("REJECT_TRANSACTION_APPROVAL"),

    //입금 확인 요청
    DEPOSIT_CONFIRM_REQUEST("DEPOSIT_CONFIRM_REQUEST"),

    //입금 확인 완료
    CONFIRM_DEPOSIT("CONFIRM_DEPOSIT"),

    //거래승인
    TRANSACTION_APPROVAL("TRANSACTION_APPROVAL"),

    //판매완료
    SALES_COMPLETE("SALES_COMPLETE"),

    //1:1 문의 답변
    INQUIRY_RESPONSE("INQUIRY_RESPONSE"),

    //제재 해제 알림
    RELEASE_RESTRICT("RELEASE_RESTRICT"),

    //제재 알림
    RESTRICT("RESTRICT"),

    ETC("ETC");

    NotiSubType(String subType) {
        this.subType = subType;
    }

    private String subType;

    @Override
    public String toString() {
        return this.subType;
    }

    @MappedTypes(NotiSubType.class)
    public static class TypeHandler extends CodeEnumTypeHandler<NotiSubType> {
        public TypeHandler() {
            super(NotiSubType.class);
        }
    }

}
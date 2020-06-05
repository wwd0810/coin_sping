package com.laon.cashlink.common.define.market;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum PurchaseStatus implements CodeEnum {

    // 구매 승인 대기
    WAITING_FOR_APPROVAL("WAITING_FOR_APPROVAL"),

    // 구매 거부
    DENY("DENY"),

    // 구매 승인 완료 / 입금 대기중
    WAITING_FOR_DEPOSIT("WAITING_FOR_DEPOSIT"),

    // 입금 완료
    DEPOSIT_COMPLETED("DEPOSIT_COMPLETED"),

    // 완료
    DONE("DONE"),

    // 취소
    CANCEL("CANCEL");

    PurchaseStatus(String status) {
        this.status = status;
    }

    private String status;

    @Override
    public String toString() {
        return this.status;
    }

    @MappedTypes(PurchaseStatus.class)
    public static class TypeHandler extends CodeEnumTypeHandler<PurchaseStatus> {
        public TypeHandler() {
            super(PurchaseStatus.class);
        }
    }

}
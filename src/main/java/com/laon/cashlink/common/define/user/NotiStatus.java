package com.laon.cashlink.common.define.user;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum NotiStatus implements CodeEnum {

    NOT_READ("NOT_READ"),

    READ("READ");

    NotiStatus(String status) {
        this.status = status;
    }

    private String status;

    @Override
    public String toString() {
        return this.status;
    }

    @MappedTypes(NotiStatus.class)
    public static class TypeHandler extends CodeEnumTypeHandler<NotiStatus> {
        public TypeHandler() {
            super(NotiStatus.class);
        }
    }

}
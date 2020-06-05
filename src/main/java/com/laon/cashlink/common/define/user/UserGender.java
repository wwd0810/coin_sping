package com.laon.cashlink.common.define.user;

import com.laon.cashlink.common.define.CodeEnum;
import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum UserGender implements CodeEnum {

    MAN("MAN"),

    WOMAN("WOMAN");

    UserGender(String gender) {
        this.gender = gender;
    }

    private String gender;

    @Override
    public String toString() {
        return this.gender;
    }

    @MappedTypes(UserGender.class)
    public static class TypeHandler extends CodeEnumTypeHandler<UserGender> {
        public TypeHandler() {
            super(UserGender.class);
        }
    }

}
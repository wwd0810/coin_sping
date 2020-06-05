package com.laon.cashlink.common.define;

import com.laon.cashlink.common.lib.CodeEnumTypeHandler;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
public enum PolicyCode implements CodeEnum {

    APPLICATION_ANDROID_REQUIRE_VERSION("application.android.require_version"),

    APPLICATION_ANDROID_VERSION("application.android.version"),

    APPLICATION_IOS_REQUIRE_VERSION("application.ios.require_version"),

    APPLICATION_IOS_VERSION("application.ios.version"),

    MARKET_BUY_USER_APPLY_LIMIT("market.buy.user_apply_limit"),

    MARKET_CONDITION("market.condition"),

    MARKET_CONDITION_LOWER("market.condition_lower"),

    MARKET_CONDITION_UPPER("market.condition_upper"),

    MARKET_FEES("market.fees"),

    MARKET_SALE_MIN("market.sale.min"),

    MARKET_SALE_MAX("market.sale.max"),

    MARKET_SALE_STEP("market.sale.step"),

    MARKET_SALE_USER_APPLY_LIMIT("market.sale.user_apply_limit"),

    TERMS_SERVICE("terms.service"),

    TERMS_E_FINANCIAL("terms.e-financial"),

    TERMS_COLLECT_INFO("terms.collect-info"),

    TERMS_PRIVACY("terms.privacy"),

    TERMS_THIRD("terms.third");

    PolicyCode(String name) {
        this.name = name;
    }

    private String name;

    @Override
    public String toString() {
        return this.name;
    }

    @MappedTypes(PolicyCode.class)
    public static class TypeHandler extends CodeEnumTypeHandler<PolicyCode> {
        public TypeHandler() {
            super(PolicyCode.class);
        }
    }

}
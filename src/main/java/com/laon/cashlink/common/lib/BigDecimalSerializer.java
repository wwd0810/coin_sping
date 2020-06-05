package com.laon.cashlink.common.lib;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import java.io.IOException;
import java.math.BigDecimal;

public class BigDecimalSerializer extends StdSerializer<BigDecimal> {

    private static final long serialVersionUID = 6665208012648144865L;

    protected BigDecimalSerializer(Class<BigDecimal> t) {
        super(t);
    }

    public BigDecimalSerializer() {
        this(null);
    }

    @Override
    public void serialize(BigDecimal value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(value.toPlainString());
    }

}

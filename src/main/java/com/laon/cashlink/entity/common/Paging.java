package com.laon.cashlink.entity.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Paging {

    @Builder.Default
    private Long page = 0L;

    @Builder.Default
    private Integer limit = 10;

    @Builder.Default
    private Long count = 0L;

    public Long getOffset() {
        return this.page * this.limit;
    }

}
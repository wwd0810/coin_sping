package com.laon.cashlink.entity.dto.common;

import lombok.*;

public class ApplyServiceInfo {

    @Data
    @Builder
    @ToString
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Request {

        private String service_name_ko;
        private String service_name_en;

        private String company_name_ko;
        private String company_name_en;

        private String owner_name_ko;
        private String owner_name_en;

        private String registration_num;
        private String mail_num;

        private String address_ko;
        private String address_en;

        private String phone;
        private String fax;

        private String privacy_officer;
        private String admin_email;

    }

}

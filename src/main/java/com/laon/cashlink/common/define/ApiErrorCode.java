package com.laon.cashlink.common.define;

import lombok.Getter;

@Getter
public enum ApiErrorCode {

    // USER
    USER_NOT_FOUND("api.error.user_not_found", "유저를 찾을 수 없습니다."),

    ALREADY_EXISTS_PHONE("api.error.already_exists_phone", "이미 존재하는 전화번호입니다."),

    PIN_NOT_MATCH("api.error.pin_not_match", "PIN 비밀번호가 올바르지 않습니다."),

    //// ACCOUNT
    ACCOUNT_NOT_FOUND("api.error.account_not_found", "계좌를 찾을 수 없습니다."),

    NOT_ENOUGH_DL("api.error.not_enough_dl", "DL이 모자릅니다."),

    NOT_ENOUGH_CP("api.error.not_enough_cp", "CP가 모자릅니다."),

    // MARKET
    MARKET_NOT_FOUND("api.error.market_not_found", "상품을 찾을 수 없습니다."),

    ALREADY_IN_PROGRESS("api.error.already_in_progress", "이미 구매가 진행중인 상품입니다."),

    ALREADY_PAID_ITEM("api.error.already_paid_item", "이미 판매 완료된 상품압니다."),

    CANNOT_BE_PURCHASE("api.error.cannot_be_purchase", "구매 할 수 없는 상품입니다."),



    //// PURCHASE

    SAME_PREVIOUS_COUNT("api.error.same_previous_count", "이전 등록 갯수와 같습니다."),

    PURCHASE_NOT_FOUND("api.error.purchase_not_found", "구매이력을 찾을 수 없습니다."),

    CANNOT_BE_CANCELED("api.error.cannot_be_canceled", "취소할 수 없습니다."),

    CANNOT_BE_ACCEPT("api.error.cannot_be_accept", "수락할 수 없습니다."),

    CANNOT_BE_DONE("api.error.cannot_be_done", "완료처리가 불가능힙니다."),

    OUT_OF_RANGE("api.error.out_of_range", "상한가/하한가 범위를 벗어났습니다."),

    TOO_MANY_SALES("api.error.too_many_sales", "판매가능 갯수를 초과하였습니다. 판매가능 갯수는 최대 10개입니다."),

    ALREADY_EXISTS_REPORT("api.error.already_exists_report", "이미 신고 되었습니다."),

    // INQUIRY
    INQUIRY_NOT_FOUND("api.error.inquiry_not_found", "문의사항을 찾을 수 없습니다."),

    // NOTICE
    NOTICE_NOT_FOUND("api.error.notice_not_found", "공지사항을 찾을 수 없습니다."),

    // FAQ
    FAQ_NOT_FOUND("api.error.faq_not_found", " FAQ를 찾을 수 없습니다."),

    // SECURITY
    NOT_AUTHORIZED("api.error.not_authorized", "로그인이 필요합니다."),

    LOGIN_FAILURE("api.error.login_failure", "아이디 또는 비밀번호를 확인해주세요."),

    CREDENTIALS_IS_INVALID("api.error.credentials_is_invalid", "인증이 유효하지 않습니다."),

    // SERVICE INFO
    SERVICE_INFO_NOT_FOUND("api.error.service_info_not_found", "서비스 정보가 없습니다."),

    // COMMON
    INVALID_PARAMETER("api.error.invalid_parameter", "파라미터가 잘못되었습니다."),

    PERMISSION_DENIED("api.error.permission_denied", "해당 요청을 진행할 권한이 없습니다."),

    PAGE_NOT_FOUND("api.error.page_not_found", "페이지를 찾을 수 없습니다."),

    METHOD_NOT_ALLOWED("api.error.method_not_allowed", "지원하지 않는 요청 메소드입니다."),

    CLIENT_ABORT("api.error.client_abort", "사용자가 연결을 종료했습니다."),

    NOT_AVAILABLE("api.error.not_available", "불가능합니다."),

    POLICY_NOT_FOUND("api.error.policy_not_found", "정책을 찾을 수 없습니다."),

    UNKNOWN("api.error.unknown", "알 수 없는 에러입니당.");

    ApiErrorCode(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private String code;
    private String msg;
}
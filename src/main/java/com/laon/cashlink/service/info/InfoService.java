package com.laon.cashlink.service.info;

import java.util.Map;

public interface InfoService {

    Map<String, Object> readApplicationInfo() throws Exception;

    Map<String, Object> readTerms(String type) throws Exception;

}
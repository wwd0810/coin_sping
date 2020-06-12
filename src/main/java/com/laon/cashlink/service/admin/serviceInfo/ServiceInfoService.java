package com.laon.cashlink.service.admin.serviceInfo;

import com.laon.cashlink.entity.dto.common.ApplyServiceInfo;
import com.laon.cashlink.entity.user.User;

import java.util.Map;

public interface ServiceInfoService {

    Map<String, Object> readServiceInfo() throws Exception;

    Map<String, Object> updateSerticeInfo(ApplyServiceInfo.Request request) throws Exception;

}

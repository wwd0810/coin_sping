package com.laon.cashlink.service.admin.serviceInfo;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.ServiceInfo;
import com.laon.cashlink.entity.dto.common.ApplyServiceInfo;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.ServiceInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.Map;

@Service("ServiceInfoService")
@RequiredArgsConstructor
public class ServiceInfoServiceImpl implements ServiceInfoService {

    private final ServiceInfoRepository serviceInfoRepository;

    @Override
    public Map<String, Object> readServiceInfo() throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        ServiceInfo serviceInfo = serviceInfoRepository.readServiceInfo(payload);

        if (ObjectUtils.isEmpty(serviceInfo)) throw new ApiException(ApiErrorCode.SERVICE_INFO_NOT_FOUND);

        {
            returnMap.put("service_info", serviceInfo);
        }

        return returnMap;

    }

    @Override
    public Map<String, Object> updateSerticeInfo(ApplyServiceInfo.Request request) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("service_name_ko", request.getService_name_ko());
            payload.put("service_name_en", request.getService_name_en());
            payload.put("company_name_ko", request.getCompany_name_ko());
            payload.put("company_name_en", request.getCompany_name_en());
            payload.put("owner_name_ko", request.getOwner_name_ko());
            payload.put("owner_name_en", request.getOwner_name_en());
            payload.put("registration_num", request.getRegistration_num());
            payload.put("mail_num", request.getMail_num());
            payload.put("address_ko", request.getAddress_ko());
            payload.put("address_en", request.getAddress_en());
            payload.put("phone", request.getPhone());
            payload.put("fax", request.getFax());
            payload.put("privacy_officer", request.getPrivacy_officer());
            payload.put("admin_email", request.getAdmin_email());
        }

        serviceInfoRepository.updateServiceInfo(payload);

        return returnMap;

    }

}

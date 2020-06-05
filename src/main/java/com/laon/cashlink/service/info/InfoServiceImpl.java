package com.laon.cashlink.service.info;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Policy;
import com.laon.cashlink.repository.common.PolicyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service("InfoService")
@RequiredArgsConstructor
class InfoServiceImpl implements InfoService {

    private final PolicyRepository policyRepository;

    @Override
    public Map<String, Object> readApplicationInfo() throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("key", "application.");
        }
        List<Policy> policyList = policyRepository.readPolicyList(payload);

        policyList.forEach(item -> {
            returnMap.put(item.getKey(), item.getValue());
        });

        return returnMap;
    }

    @Override
    public Map<String, Object> readTerms(String type) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("key", "terms." + type);
        }
        Policy policy = policyRepository.readPolicy(payload);
        if (ObjectUtils.isEmpty(policy)) throw new ApiException(ApiErrorCode.POLICY_NOT_FOUND);

        {
            returnMap.put("data", policy.getValue());
        }
        return returnMap;
    }

}
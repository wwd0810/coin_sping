package com.laon.cashlink.service.admin.policy;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.common.Policy;
import com.laon.cashlink.entity.dto.policy.PolicyDTO;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.PolicyRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service("AdminPolicyService")
@RequiredArgsConstructor
public class AdminPolicyServiceImpl implements AdminPolicyService {

    private final PolicyRepository policyRepository;

    @Override
    public Map<String, Object> readPolicyList(Long page) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder().page(page).build();

        {
            payload.put("paging", paging);
        }
        List<Policy> policyList = policyRepository.readPolicyList(payload);
        Long count = policyRepository.countPolicyList(payload);

        paging.setCount(count);

        {
            returnMap.put("list", policyList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> createPolicy(PolicyDTO.Request request, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("key", request.getKey());
            payload.put("value", request.getValue());
            payload.put("description", request.getDescription());
            payload.put("created_by", user.getId());
        }
        policyRepository.createPolicy(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> updatePolicy(PolicyDTO.Request request, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("key", request.getKey());
        }
        Policy policy = policyRepository.readPolicy(payload);
        if (ObjectUtils.isEmpty(policy)) throw new ApiException(ApiErrorCode.POLICY_NOT_FOUND);

        {
            payload.put("key", policy.getKey());
            payload.put("value", request.getValue());
            payload.put("description", request.getDescription());
            payload.put("updated_by", user.getId());
        }
        policyRepository.updatePolicy(payload);

        return returnMap;
    }

}

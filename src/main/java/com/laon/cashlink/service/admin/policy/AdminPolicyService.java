package com.laon.cashlink.service.admin.policy;

import com.laon.cashlink.entity.dto.policy.PolicyDTO;
import com.laon.cashlink.entity.user.User;
import java.util.Map;

public interface AdminPolicyService {

    Map<String, Object> readPolicyList(Long page) throws Exception;

    Map<String, Object> createPolicy(PolicyDTO.Request request, User user) throws Exception;

    Map<String, Object> updatePolicy(PolicyDTO.Request request, User user) throws Exception;

}

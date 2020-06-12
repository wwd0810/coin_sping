package com.laon.cashlink.service.admin.user;

import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.user.User;

import com.laon.cashlink.entity.user.UserList;
import com.laon.cashlink.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("AdminUserService")
@RequiredArgsConstructor
public class AdminUserServiceImpl implements AdminUserService {

    private final UserRepository userRepository;

    @Override
    public Map<String, Object> readUserList(User user, Long page, Integer size, String orderType, String query, String duration)
            throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder().page(page).build();

        Order order = Order.parse(orderType);

        {
            if (!ObjectUtils.isEmpty(user)) {
                payload.put("user", user);
            }
            payload.put("query", query);
            payload.put("duration", duration);
            payload.put("order", order);
            payload.put("paging", paging);
        }

        List<User> userList = userRepository.readUserList(payload);
        Long count = userRepository.countUserList(payload);
        paging.setCount(count);

        {
            returnMap.put("content", userList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }
}

package com.laon.cashlink.service.admin.user;


import com.laon.cashlink.entity.user.User;
import java.util.Map;

public interface AdminUserService {


    Map<String, Object> readUserList(User user, Long page, Integer size, String order, String query, String duration) throws Exception;

}

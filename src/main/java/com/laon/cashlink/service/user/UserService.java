package com.laon.cashlink.service.user;

import com.laon.cashlink.common.define.user.NotiStatus;
import com.laon.cashlink.common.define.user.NotiSubType;
import com.laon.cashlink.common.define.user.NotiType;
import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.dto.Remit;
import com.laon.cashlink.entity.user.User;
import org.springframework.util.ObjectUtils;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface UserService {

    Map<String, Object> searchUser(String type, String query) throws Exception;

    Map<String, Object> readMyInfo(String username) throws Exception;

    Map<String, Object> readMyAccount(User user) throws Exception;

    Map<String, Object> remit(User user, Remit.Request request) throws Exception;

    Map<String, Object> readAccountTx(String account_id, Long page, User user, String duration, String status) throws Exception;

    Map<String, Object> readMyNotification(User user, Long page, NotiType type, NotiSubType sub_type, NotiStatus status) throws Exception;

    Map<String, Object> checkUnReadNoti(User user) throws Exception;

    Map<String, Object> readMyNoti(User user, Long noti_id) throws Exception;

    Map<String, Object> deleteMyNoti(User user, Long noti_id) throws Exception;

    Map<String, Object> readMyNotiAll(User user) throws Exception;

    Map<String, Object> updateUserToken(User user, String token) throws Exception;

    Map<String, Object> syncUser(Principal principal) throws Exception;

    Map<String, Object> readMyMarketStatus(User user) throws Exception;

    //purchase
    Map<String, Object> readMyPurchases(User user, Long page) throws Exception;

    // PIN Password
    Map<String, Object> duplicatePinPass(User user, String password) throws Exception;

    Map<String, Object> updatePinPass(User user, String password) throws Exception;

}
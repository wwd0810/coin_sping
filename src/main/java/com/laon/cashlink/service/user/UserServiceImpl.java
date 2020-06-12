package com.laon.cashlink.service.user;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.define.DeleteFlag;
import com.laon.cashlink.common.define.PolicyCode;
import com.laon.cashlink.common.define.market.MarketStatus;
import com.laon.cashlink.common.define.market.PurchaseStatus;
import com.laon.cashlink.common.define.user.AccountType;
import com.laon.cashlink.common.define.user.NotiStatus;
import com.laon.cashlink.common.define.user.NotiSubType;
import com.laon.cashlink.common.define.user.NotiType;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.common.Policy;
import com.laon.cashlink.entity.dto.Remit;
import com.laon.cashlink.entity.market.Market;
import com.laon.cashlink.entity.market.Purchase;
import com.laon.cashlink.entity.user.Account;
import com.laon.cashlink.entity.user.Notification;
import com.laon.cashlink.entity.user.Transaction;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.PolicyRepository;
import com.laon.cashlink.repository.market.MarketLikeRepository;
import com.laon.cashlink.repository.market.MarketRepository;
import com.laon.cashlink.repository.market.PurchaseRepository;
import com.laon.cashlink.repository.user.AccountRepository;
import com.laon.cashlink.repository.user.NotiRepository;
import com.laon.cashlink.repository.user.UserRepository;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.Principal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

// @Slf4j
@Service("UserService")
@RequiredArgsConstructor
class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    private final NotiRepository notiRepository;

    private final PolicyRepository policyRepository;

    private final PurchaseRepository purchaseRepository;

    private final MarketRepository marketRepository;
    private final MarketLikeRepository marketLikeRepository;






    @Override
    public Map<String, Object> searchUser(String type, String query) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            if ("PHONE".equals(type))
                payload.put("phone", query);
            else if ("USERNAME".equals(type))
                payload.put("username", query);
        }
        User user = userRepository.readUser(payload);
        if (ObjectUtils.isEmpty(user))
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);

        {
            payload.clear();
            payload.put("user_id", user.getId());
        }
        List<Account> accountList = accountRepository.readAccountList(payload);

        {
            returnMap.put("list", accountList);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyInfo(String username) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("username", username);
        }
        User user = userRepository.readUser(payload);
        if (ObjectUtils.isEmpty(user)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

        {
            payload.clear();
            payload.put("user_id", user.getId());
        }
        List<Account> account = accountRepository.readAccountList(payload);
        if (ObjectUtils.isEmpty(account)) {
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("status", NotiStatus.NOT_READ);
        }
        boolean checkNotReadNoti = notiRepository.checkNotReadNoti(payload);

        {
            payload.clear();
            payload.put("user_id", user.getId());
        }

        boolean checkUserPin = userRepository.checkUserPin(payload);

        Map<String, Object> other = new HashMap<>();
        {
            other.put("unread_noti", checkNotReadNoti);
            other.put("check_pin", checkUserPin);
        }

        {
            returnMap.put("user", user);
            returnMap.put("account", account);
            returnMap.put("other", other);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyAccount(User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
        }
        List<Account> account = accountRepository.readAccountList(payload);

        {
            returnMap.put("account", account);
        }
        return returnMap;
    }

    @Override
    @Transactional
    public Map<String, Object> remit(User user, Remit.Request request) throws Exception {

        if (request.getFrom().equals(request.getTo())) {
            throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
        }

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(request.getPassword().getBytes());

        byte[] bytes = md.digest();



        {
            payload.put("password", Hex.encodeHexString(bytes));
            payload.put("user_id", user.getId());
        }

        Boolean duplicate = userRepository.duplicatePinPass(payload);

        if(!duplicate) {
            throw new ApiException(ApiErrorCode.PIN_NOT_MATCH);
        }

        {
            payload.clear();
            payload.put("account_id", request.getFrom());
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.DL);
        }
        Account fromDL = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(fromDL))
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("account_id", request.getTo());
        }
        Account to = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(to))
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        if (fromDL.getQuantity().compareTo(request.getAmount()) == -1) {
            throw new ApiException(ApiErrorCode.NOT_ENOUGH_DL);
        }

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.CP);
        }
        Account fromCP = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(fromCP))
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_FEES);
        }
        Policy marketFee = policyRepository.readPolicy(payload);
        BigDecimal fee = new BigDecimal(marketFee.getValue());

        BigDecimal fees = request.getAmount().multiply(fee);

        if (fromCP.getQuantity().compareTo(fees) == -1) {
            throw new ApiException(ApiErrorCode.NOT_ENOUGH_CP);
        }

        {
            payload.clear();
            payload.put("user_id", to.getUser_id());
        }
        User target = userRepository.readUser(payload);
        if (ObjectUtils.isEmpty(target))
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);

        {
            payload.clear();
            payload.put("account_id", fromDL.getId());
            payload.put("quantity", fromDL.getQuantity().subtract(request.getAmount()));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("from", fromDL.getId());
            payload.put("to", to.getId());
            payload.put("title", "DL 전송");
            payload.put("amount", request.getAmount());
        }
        accountRepository.createTransaction(payload);

        {
            payload.clear();
            payload.put("account_id", fromCP.getId());
            payload.put("quantity", fromCP.getQuantity().subtract(fees));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("from", fromCP.getId());
            payload.put("title", "수수료 차감");
            payload.put("description", request.getType() + "전송 수수료");
            payload.put("amount", fees.multiply(new BigDecimal(-1)));
        }
        accountRepository.createTransaction(payload);

        {
            payload.clear();
            payload.put("account_id", to.getId());
            payload.put("quantity", to.getQuantity().add(request.getAmount()));
        }
        accountRepository.updateAccount(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> readAccountTx(String account_id, Long page, User user, String duration, String status) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("account_id", account_id);
        }
        Account account = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(account))
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        if (!account.getUser_id().equals(user.getId()))
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        Paging paging = Paging.builder().page(page).limit(10).build();
        {
            payload.clear();
            payload.put("account_id", account.getId());
            payload.put("duration", duration);
            payload.put("status", status);
            payload.put("paging", paging);
        }
        List<Transaction> txList = accountRepository.readTransactionList(payload);
        Long count = accountRepository.countTransactionList(payload);

        paging.setCount(count);

        {
            returnMap.put("list", txList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyNotification(User user, Long page, NotiType type, NotiSubType sub_type,
            NotiStatus status) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder().page(page).build();
        {
            payload.put("user_id", user.getId());
            payload.put("type", type);
            payload.put("sub_type", sub_type);
            payload.put("status", status);
            payload.put("paging", paging);
        }

        List<Notification> noti = notiRepository.readNotiList(payload);
        Long count = notiRepository.countNotiList(payload);
        paging.setCount(count);

        {
            returnMap.put("noti", noti);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> checkUnReadNoti(User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
            payload.put("status", NotiStatus.NOT_READ);
        }

        boolean exists = notiRepository.checkNotReadNoti(payload);

        {
            returnMap.put("exists", exists);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyNoti(User user, Long noti_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("noti_id", noti_id);
        }
        Notification noti = notiRepository.readNoti(payload);
        if (!ObjectUtils.isEmpty(noti)) {
            if (!noti.getUser_id().equals(user.getId())) {
                throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
            }

            {
                payload.clear();
                payload.put("noti_id", noti.getId());
                payload.put("status", NotiStatus.READ);
            }
            notiRepository.updateNoti(payload);
        }

        return returnMap;
    }

    @Override
    public Map<String, Object> deleteMyNoti(User user, Long noti_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("noti_id", noti_id);
        }
        Notification noti = notiRepository.readNoti(payload);
        if (!ObjectUtils.isEmpty(noti)) {
            if (!noti.getUser_id().equals(user.getId())) {
                throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
            }

            {
                payload.clear();
                payload.put("noti_id", noti.getId());
                payload.put("delete_yn", DeleteFlag.Y);
            }
            notiRepository.updateNoti(payload);
        }

        return returnMap;
    }

    @Override
    public Map<String, Object> readMyNotiAll(User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
            payload.put("status", NotiStatus.READ);
        }
        notiRepository.updateNoti(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> updateUserToken(User user, String token) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
            payload.put("token", token);
        }
        userRepository.updateUser(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> syncUser(Principal principal) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        Map<String, Object> map = (Map<String, Object>) ((OAuth2Authentication) principal).getUserAuthentication()
                .getDetails();
        {
            payload.put("username", map.get("username"));
        }

        User user = userRepository.readUser(payload);

        {
            payload.clear();
            payload.put("username", map.get("username"));
            payload.put("name", map.get("name"));
            payload.put("phone", map.get("phone"));
            payload.put("birth", LocalDate.parse((String) map.get("birth"), DateTimeFormatter.ISO_DATE));
            payload.put("sex", map.get("sex"));
        }
        userRepository.updateUser(payload);

        {
            payload.clear();
            payload.put("user_id", user.getId());
        }
        user = userRepository.readUser(payload);

        {
            returnMap.put("user", user);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyMarketStatus(User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        {
            payload.put("status", MarketStatus.INIT);
            payload.put("seller_id", user.getId());
        }
        Long sellCount = marketRepository.countMarketList(payload);
        {
            payload.clear();
            payload.put("purchase_status", PurchaseStatus.WAITING_FOR_APPROVAL);
            payload.put("buyer_id", user.getId());
        }
        Long buyCount = purchaseRepository.countPurchaseList(payload);
        {
            payload.clear();
            payload.put("user_id", user.getId());
        }
        Long likeCount = marketLikeRepository.countLike(payload);

        {
            returnMap.put("sell", sellCount);
            returnMap.put("buy", buyCount);
            returnMap.put("like", likeCount);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMyPurchases(User user, Long page) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder().page(page).build();

        {
            payload.put("user_id", user.getId());
            payload.put("paging", paging);
        }
        List<Purchase> purchaseList = purchaseRepository.readPurchaseList(payload);
        Long count = purchaseRepository.countPurchaseList(payload);
        paging.setCount(count);

        {
            returnMap.put("list", purchaseList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }


    /*
    * Pin Password 관련
    * */

    @Override
    public Map<String, Object> duplicatePinPass(User user, String password) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte[] bytes = md.digest();

        {
            payload.put("user_id", user.getId());
            payload.put("password", Hex.encodeHexString(bytes));
        }

        Boolean duplicate = userRepository.duplicatePinPass(payload);

        if (!duplicate) {
            throw new ApiException(ApiErrorCode.PIN_NOT_MATCH);
        }

        {
            returnMap.put("duplicate", true);
        }

        return returnMap;

    }

    @Override
    public Map<String, Object> updatePinPass(User user, String password) throws Exception {

        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());

        byte[] bytes = md.digest();
        {
            payload.put("user_id", user.getId());
            payload.put("password", Hex.encodeHexString(bytes));
        }

        userRepository.updatePinPass(payload);

        return returnMap;

    }
}
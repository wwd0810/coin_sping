package com.laon.cashlink.service.market;

import com.laon.cashlink.common.define.ApiErrorCode;
import com.laon.cashlink.common.define.PolicyCode;
import com.laon.cashlink.common.define.market.MarketStatus;
import com.laon.cashlink.common.define.market.PurchaseStatus;
import com.laon.cashlink.common.define.user.AccountType;
import com.laon.cashlink.common.exception.ApiException;
import com.laon.cashlink.entity.common.Order;
import com.laon.cashlink.entity.common.Paging;
import com.laon.cashlink.entity.common.Policy;
import com.laon.cashlink.entity.dto.market.MarketSale;
import com.laon.cashlink.entity.market.Market;
import com.laon.cashlink.entity.market.Purchase;
import com.laon.cashlink.entity.market.Report;
import com.laon.cashlink.entity.user.Account;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.common.PolicyRepository;
import com.laon.cashlink.repository.google.FirebaseRepository;
import com.laon.cashlink.repository.market.MarketLikeRepository;
import com.laon.cashlink.repository.market.MarketRepository;
import com.laon.cashlink.repository.market.PurchaseRepository;
import com.laon.cashlink.repository.user.AccountRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.security.MessageDigest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.laon.cashlink.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Hex;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

// @Slf4j
@Service("MarketService")
@Transactional
@RequiredArgsConstructor
class MarketServiceImpl implements MarketService {

    private final AccountRepository accountRepository;
    private final MarketRepository marketRepository;
    private final PurchaseRepository purchaseRepository;
    private final PolicyRepository policyRepository;
    private final FirebaseRepository firebaseRepository;

    private final MarketLikeRepository marketLikeRepository;

    private final UserRepository userRepository;

    @Override
    public Map<String, Object> readMarketList(User user, Long page, Integer size, String orderType, String query)
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
            payload.put("order", order);
            payload.put("statuses", Arrays.asList(
                    MarketStatus.INIT,
                    MarketStatus.ON_SALE
            ));
        }

        List<Market> marketList = marketRepository.readMarketList(payload);
        Long count = marketRepository.countMarketList(payload);
        paging.setCount(count);

        {
            returnMap.put("content", marketList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> readMarket(Long market_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }

        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) {
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);
        }

        {
            returnMap.put("market", market);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> updateMarket(Long market_id, MarketSale.Request request, User user) throws Exception {
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

        if (!duplicate) {
            throw new ApiException(ApiErrorCode.PIN_NOT_MATCH);
        }

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.CP);
        }
        Account CPAccount = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(CPAccount)) {
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        if (!Arrays.asList(AccountType.DL, AccountType.DLC).contains(request.getType())) {
            throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
        }

        {
            payload.clear();
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);


        if (ObjectUtils.isEmpty(market))
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId()))
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        switch (market.getStatus()) {
            case ON_SALE:
            case DONE:
            case EXPIRED:
            case CANCEL:
                throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
            default:
        }


        BigDecimal qty = request.getAmount();
        BigDecimal bf_qty = market.getQuantity();

        if (qty.equals(bf_qty)) {
            throw new ApiException(ApiErrorCode.SAME_PREVIOUS_COUNT);
        }

        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_FEES);
        }
        Policy marketFee = policyRepository.readPolicy(payload);
        BigDecimal fee = new BigDecimal(marketFee.getValue());

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", request.getType());
        }
        Account DLAccount = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(DLAccount)) {
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);
        }


        BigDecimal pri = request.getPrice();
        BigDecimal fees = qty.multiply(pri).multiply(fee).divide(new BigDecimal(100), 3, RoundingMode.CEILING);


        BigDecimal bf_pri = market.getPrice();
        BigDecimal bf_fees = bf_qty.multiply(bf_pri).multiply(fee).divide(new BigDecimal(100), 3, RoundingMode.CEILING);

        int typeCheck = qty.compareTo(bf_qty);

        if (typeCheck != 0) {
            {
                payload.clear();
                if (typeCheck > 0) {
                    payload.put("from", DLAccount.getId());
                    payload.put("title", "DL 구매");
                    payload.put("amount", qty.subtract(bf_qty).multiply(new BigDecimal(-1)));
                } else {
                    payload.put("to", DLAccount.getId());
                    payload.put("title", "DL 환불");
                    payload.put("amount", bf_qty.subtract(qty));
                }
            }

            accountRepository.createTransaction(payload);

            {
                payload.clear();
                payload.put("user_id", user.getId());
                payload.put("account_type", AccountType.DL);
                if (typeCheck > 0) {
                    payload.put("quantity", DLAccount.getQuantity().subtract(qty.subtract(bf_qty)));
                } else {
                    payload.put("quantity", DLAccount.getQuantity().add(bf_qty.subtract(qty)));
                }

            }


            accountRepository.updateAccount(payload);

            {
                payload.clear();
                if (typeCheck > 0) {
                    payload.put("from", CPAccount.getId());
                    payload.put("title", "수수료 차감");
                    payload.put("amount", fees.subtract(bf_fees).multiply(new BigDecimal(-1)));
                } else {
                    payload.put("to", CPAccount.getId());
                    payload.put("title", "CP 환불");
                    payload.put("amount", bf_fees.subtract(fees));
                }

            }


            accountRepository.createTransaction(payload);


            {
                payload.clear();
                payload.put("user_id", user.getId());
                payload.put("account_type", AccountType.CP);
                if (typeCheck > 0) {
                    payload.put("quantity", CPAccount.getQuantity().subtract(fees.subtract((bf_fees))));
                } else {
                    payload.put("quantity", CPAccount.getQuantity().add(bf_fees.subtract(fees)));
                }
            }


            accountRepository.updateAccount(payload);
        }


        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("quantity", qty);
            payload.put("price", pri);
            payload.put("fees", fees);
        }

        marketRepository.updateMarket(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> acceptPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market))
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId()))
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        switch (market.getStatus()) {
            case INIT:
            case EXPIRED:
            case DONE:
            case CANCEL:
                throw new ApiException(ApiErrorCode.CANNOT_BE_ACCEPT);
            default:
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase))
            throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        switch (purchase.getStatus()) {
            case WAITING_FOR_DEPOSIT:
            case DEPOSIT_COMPLETED:
            case DONE:
            case CANCEL:
                throw new ApiException(ApiErrorCode.CANNOT_BE_ACCEPT);
            default:
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase.getId());
            payload.put("status", PurchaseStatus.WAITING_FOR_DEPOSIT);
        }
        purchaseRepository.updatePurchase(payload);

        {
            payload.clear();
            payload.put("user_id", purchase.getBuyer().getId());
        }

        User buyer = userRepository.readUser(payload);

        if (ObjectUtils.isEmpty(buyer)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

        firebaseRepository.send(buyer.getToken(), "캐시링크", "고객님이 구매 신청하신 상품에 대한 새로운 알림이 있습니다. 자세한 내용을 보려면 탭하세요.");

        // 여기 추가
         return returnMap;
    }

    @Override
    public Map<String, Object> denyPurchaseRequest(Long market_id, Long purchase_id, String reason, User user)
            throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market))
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId()))
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        switch (market.getStatus()) {
            case INIT:
            case EXPIRED:
            case DONE:
            case CANCEL:
                throw new ApiException(ApiErrorCode.CANNOT_BE_ACCEPT);
            default:
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase))
            throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        switch (purchase.getStatus()) {
            case WAITING_FOR_DEPOSIT:
            case DEPOSIT_COMPLETED:
            case DONE:
            case CANCEL:
                throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
            default:
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase.getId());
            payload.put("status", PurchaseStatus.DENY);
            payload.put("reason", reason);
            payload.put("canceled_at", LocalDateTime.now());
        }
        purchaseRepository.updatePurchase(payload);

        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("status", MarketStatus.INIT);
        }
        marketRepository.updateMarket(payload);

        {
            payload.clear();
            payload.put("user_id", purchase.getBuyer().getId());
        }

        User buyer = userRepository.readUser(payload);

        if (ObjectUtils.isEmpty(buyer)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

      firebaseRepository.send(buyer.getToken(), "캐시링크", "고객님이 구매 신청하신 상품에 대한 새로운 알림이 있습니다. 자세한 내용을 보려면 탭하세요.");


        return returnMap;
    }

    @Override
    public Map<String, Object> depositPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        switch (market.getStatus()) {
            case CANCEL:
            case DONE:
            case EXPIRED:
            case INIT:
                throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase)) throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        if (!purchase.getBuyer().getId().equals(user.getId())) throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        switch (purchase.getStatus()) {
            case WAITING_FOR_APPROVAL:
            case DEPOSIT_COMPLETED:
            case DENY:
            case CANCEL:
            case DONE:
                throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase.getId());
            payload.put("status", PurchaseStatus.DEPOSIT_COMPLETED);
        }
        purchaseRepository.updatePurchase(payload);

        {
            payload.clear();
            payload.put("user_id", market.getSeller().getId());
        }

        User buyer = userRepository.readUser(payload);

        if (ObjectUtils.isEmpty(buyer)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

        firebaseRepository.send(buyer.getToken(), "캐시링크", "고객님이 구매 신청하신 상품에 대한 새로운 알림이 있습니다. 자세한 내용을 보려면 탭하세요.");

        return returnMap;
    }

    @Override
    @Transactional
    public Map<String, Object> donePurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId())) throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase)) throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        switch (purchase.getStatus()) {
            case WAITING_FOR_APPROVAL:
            case WAITING_FOR_DEPOSIT:
            case DONE:
            case DENY:
            case CANCEL:
                throw new ApiException(ApiErrorCode.CANNOT_BE_DONE);
        }

        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("approved_at", LocalDateTime.now());
            payload.put("status", MarketStatus.DONE);
        }
        marketRepository.updateMarket(payload);

        {
            payload.clear();
            payload.put("purchase_id", purchase.getId());
            payload.put("status", PurchaseStatus.DONE);
            payload.put("approved_at", LocalDateTime.now());
        }
        purchaseRepository.updatePurchase(payload);

        {
            payload.put("user_id", purchase.getBuyer().getId());
            payload.put("account_type", AccountType.DL);
        }
        Account account = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(account)) throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("user_id", market.getSeller().getId());
            payload.put("account_type", AccountType.DL);
        }
        Account sellerAccount = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(sellerAccount)) throw new ApiException(
                ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("user_id", purchase.getBuyer().getId());
            payload.put("account_id", account.getId());
            payload.put("quantity", account.getQuantity().add(market.getQuantity()));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("account_id", account.getId());
            payload.put("from", sellerAccount.getId());
            payload.put("to", account.getId());
            payload.put("title", "DL 구매");
            payload.put("description", "");
            payload.put("amount", market.getQuantity());
        }
        accountRepository.createTransaction(payload);

        {
            payload.clear();
            payload.put("user_id", purchase.getBuyer().getId());
        }

        User buyer = userRepository.readUser(payload);

        if (ObjectUtils.isEmpty(buyer)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

        firebaseRepository.send(buyer.getToken(), "캐시링크", "고객님이 구매 신청하신 상품에 대한 새로운 알림이 있습니다. 자세한 내용을 보려면 탭하세요.");

        return returnMap;
    }

    @Override
    public Map<String, Object> reportPurchaseRequest(Long market_id, Long purchase_id, String reason, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase)) throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        if (market.getSeller().getId().equals(user.getId())) {
            {
                payload.clear();
                payload.put("purchase_id", purchase.getId());
                payload.put("from", user.getId());
                payload.put("to", purchase.getBuyer().getId());
                payload.put("reason", reason);
            }
        } else if (purchase.getBuyer().getId().equals(user.getId())) {
            {
                payload.clear();
                payload.put("purchase_id", purchase.getId());
                payload.put("from", user.getId());
                payload.put("to", market.getSeller().getId());
                payload.put("reason", reason);
            }
        } else {
            throw new ApiException(ApiErrorCode.PERMISSION_DENIED);
        }

        Report report = purchaseRepository.readPurchaseReport(payload);
        if (!ObjectUtils.isEmpty(report)) throw new ApiException(ApiErrorCode.ALREADY_EXISTS_REPORT);

        purchaseRepository.createPurchaseReport(payload);

        return returnMap;
    }

    @Override
    @Transactional
    public Map<String, Object> cancelPurchase(User user, Long market_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }

        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) {
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);
        }

        if (!market.getSeller().getId().equals(user.getId())) throw new ApiException(
                ApiErrorCode.PERMISSION_DENIED);

        switch (market.getStatus()) {
            case DONE:
            case EXPIRED:
            case CANCEL:
                throw new ApiException(ApiErrorCode.CANNOT_BE_CANCELED);
            default:
        }

        {
            payload.clear();
            payload.put("user_id", market.getSeller().getId());
            payload.put("account_type", AccountType.DL);
        }
        Account DLAccount = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(DLAccount)) throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("user_id", market.getSeller().getId());
            payload.put("account_type", AccountType.CP);
        }
        Account CPAccount = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(CPAccount)) throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("account_id", DLAccount.getId());
            payload.put("quantity", DLAccount.getQuantity().add(market.getQuantity()));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("to", DLAccount.getId());
            payload.put("title", "DL 환불");
            payload.put("amount", market.getQuantity());
        }
        accountRepository.createTransaction(payload);

        {
            payload.clear();
            payload.put("account_id", CPAccount.getId());
            payload.put("quantity", CPAccount.getQuantity().add(market.getFees()));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("to", CPAccount.getId());
            payload.put("title", "CP 환불");
            payload.put("amount", market.getFees());
        }
        accountRepository.createTransaction(payload);

        {
            payload.put("market_id", market.getId());
            payload.put("status", MarketStatus.CANCEL);
            payload.put("canceled_at", LocalDateTime.now());
        }
        marketRepository.updateMarket(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> readPurchaseList(Long market_id, Long page, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId())) throw new ApiException(
                ApiErrorCode.PERMISSION_DENIED);

        Paging paging = Paging.builder()
                .page(page)
                .build();
        {
            payload.clear();
            payload.put("market_id", market.getId());
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

    @Override
    public Map<String, Object> readPurchase(Long market_id, Long purchase_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        if (!market.getSeller().getId().equals(user.getId())) throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase)) throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        {
            returnMap.put("user", purchase.getBuyer());
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> cancelPurchaseRequest(Long market_id, Long purchase_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);

        switch (market.getStatus()) {
            case CANCEL:
            case INIT:
            case DONE:
            case EXPIRED:
                throw new ApiException(ApiErrorCode.CANNOT_BE_CANCELED);
        }

        {
            payload.clear();
            payload.put("purchase_id", purchase_id);
        }
        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (ObjectUtils.isEmpty(purchase)) throw new ApiException(ApiErrorCode.PURCHASE_NOT_FOUND);

        if (!purchase.getBuyer().getId().equals(user.getId())) throw new ApiException(ApiErrorCode.PERMISSION_DENIED);

        {
            payload.clear();
            payload.put("purchase_id", purchase.getId());
            payload.put("status", PurchaseStatus.CANCEL);
        }
        purchaseRepository.updatePurchase(payload);

        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("status", MarketStatus.INIT);
        }
        marketRepository.updateMarket(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> readUserSales(User user, Long page, String orderStr, MarketStatus status, String duration, String query)
            throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder().page(page).build();
        Order order = Order.parse(orderStr);

        {
            payload.put("seller_id", user.getId());
            payload.put("status", status);
            payload.put("duration", duration);
            payload.put("query", query);
            payload.put("order", order);
            payload.put("paging", paging);
        }

        List<Market> marketList = marketRepository.readMarketList(payload);
        Long count = marketRepository.countMarketList(payload);
        paging.setCount(count);

        {
            returnMap.put("markets", marketList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> createSale(User user, MarketSale.Request request) throws Exception {
        // TODO: 제한사항 추가(최소금액, 최대금액 확인)
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();


        {
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.CP);
        }
        Account fromCP = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(fromCP)) {
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(request.getPassword().getBytes());

        byte[] bytes = md.digest();

        {
            payload.clear();
            payload.put("password", Hex.encodeHexString(bytes));
            payload.put("user_id", user.getId());
        }

        Boolean duplicate = userRepository.duplicatePinPass(payload);

        if (!duplicate) {
            throw new ApiException(ApiErrorCode.PIN_NOT_MATCH);
        }

        if (!Arrays.asList(AccountType.DL, AccountType.DLC).contains(request.getType())) {
            throw new ApiException(ApiErrorCode.NOT_AVAILABLE);
        }

        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_SALE_USER_APPLY_LIMIT);
        }
        Policy userApplyLimit = policyRepository.readPolicy(payload);
        Integer limit = Integer.parseInt(userApplyLimit.getValue());

        MarketStatus[] arr = {MarketStatus.INIT, MarketStatus.ON_SALE};

        {
            payload.clear();
            payload.put("seller_id", user.getId());

            payload.put("statuses", arr);
        }
        Long count = marketRepository.countMarketList(payload);
        if (count >= limit)
            throw new ApiException(ApiErrorCode.TOO_MANY_SALES);

        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_CONDITION_LOWER);
        }
        Policy conditionLower = policyRepository.readPolicy(payload);
        BigDecimal lower = new BigDecimal(conditionLower.getValue());
        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_CONDITION_UPPER);
        }
        Policy conditionUpper = policyRepository.readPolicy(payload);
        BigDecimal upper = new BigDecimal(conditionUpper.getValue());

        if (lower.compareTo(request.getPrice()) == 1 || upper.compareTo(request.getPrice()) == -1) {
            throw new ApiException(ApiErrorCode.OUT_OF_RANGE);
        }

        {
            payload.clear();
            payload.put("key", PolicyCode.MARKET_FEES);
        }
        Policy marketFee = policyRepository.readPolicy(payload);
        BigDecimal fee = new BigDecimal(marketFee.getValue());

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", request.getType());
        }
        Account from = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(from)) {
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);
        }

        BigDecimal qty = request.getAmount();
        BigDecimal pri = request.getPrice();
        BigDecimal fees = qty.multiply(pri).multiply(fee).divide(new BigDecimal(100), 3, RoundingMode.CEILING);

        if (from.getQuantity().compareTo(qty) < 0) {
            throw new ApiException(ApiErrorCode.NOT_ENOUGH_DL);
        }
        if (fromCP.getQuantity().compareTo(fees) < 0) {
            throw new ApiException(ApiErrorCode.NOT_ENOUGH_CP);
        }

        {
            payload.clear();
            payload.put("quantity", qty);
            payload.put("price", pri);
            payload.put("fees", fees);
            payload.put("status", MarketStatus.INIT);
            payload.put("seller_id", user.getId());
        }
        marketRepository.createMarket(payload);

        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.DL);
            payload.put("quantity", from.getQuantity().subtract(qty));
        }
        accountRepository.updateAccount(payload);
        {
            payload.clear();
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.CP);
            payload.put("quantity", fromCP.getQuantity().subtract(fees));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("account_id", fromCP.getId());
            payload.put("title", "수수료 차감");
            payload.put("description", request.getType() + "판매 수수료");
            payload.put("amount", fees.multiply(new BigDecimal(-1)));
        }
        accountRepository.createTransaction(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> createPurchaseRequest(User user, Long market_id) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
        }
        Market market = marketRepository.readMarket(payload);
        if (ObjectUtils.isEmpty(market)) {
            throw new ApiException(ApiErrorCode.MARKET_NOT_FOUND);
        }
        if (ObjectUtils.isEmpty(market.getSeller())) {
            throw new ApiException(ApiErrorCode.CANNOT_BE_PURCHASE);
        }

        switch (market.getStatus()) {
            case ON_SALE:
                throw new ApiException(ApiErrorCode.ALREADY_IN_PROGRESS);
            case DONE:
                throw new ApiException(ApiErrorCode.ALREADY_PAID_ITEM);
            case CANCEL:
            case EXPIRED:
                throw new ApiException(ApiErrorCode.CANNOT_BE_PURCHASE);
            default:
        }

        if (market.getSeller().getId().equals(user.getId())) {
            throw new ApiException(ApiErrorCode.CANNOT_BE_PURCHASE);
        }

        {
            payload.clear();
            payload.put("market_id", market.getId());
        }

        Purchase purchase = purchaseRepository.readPurchase(payload);
        if (!ObjectUtils.isEmpty(purchase)) {
            switch (purchase.getStatus()) {
                case WAITING_FOR_APPROVAL:
                case WAITING_FOR_DEPOSIT:
                    throw new ApiException(ApiErrorCode.ALREADY_IN_PROGRESS);
                case DEPOSIT_COMPLETED:
                case DONE:
                    throw new ApiException(ApiErrorCode.ALREADY_PAID_ITEM);
                default:
            }
        }

        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("buyer_id", user.getId());
        }
        purchaseRepository.createPurchase(payload);

        {
            payload.clear();
            payload.put("market_id", market.getId());
            payload.put("status", MarketStatus.ON_SALE);
        }
        marketRepository.updateMarket(payload);

        {
            payload.clear();
            payload.put("user_id", market.getSeller().getId());
        }

        User seller = userRepository.readUser(payload);

        if (ObjectUtils.isEmpty(seller)) {
            throw new ApiException(ApiErrorCode.USER_NOT_FOUND);
        }

        firebaseRepository.send(seller.getToken(), "캐시링크", "고객님이 판매등록하신 상품에 대한 새로운 알림이 있습니다. 자세한 내용을 보려면 탭하세요.");

        return returnMap;
    }

    @Override
    public Map<String, Object> readUserPurchases(User user, Long page, String orderStr, String status, String duration, String query) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        Paging paging = Paging.builder()
                .page(page)
                .build();
        Order order = Order.parse(orderStr);

        {
            payload.put("buyer_id", user.getId());
            payload.put("order", order);
            payload.put("duration", duration);
            payload.put("status", status);
            payload.put("query", query);
            payload.put("paging", paging);
        }
        List<Market> marketList = marketRepository.readMarketList(payload);
        Long count = marketRepository.countMarketList(payload);
        paging.setCount(count);

        {
            returnMap.put("list", marketList);
            returnMap.put("paging", paging);
        }
        return returnMap;
    }

    @Override
    public Map<String, Object> toggleMarketLike(Long market_id, User user) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("market_id", market_id);
            payload.put("user_id", user.getId());
        }
        Boolean isLike = marketLikeRepository.isLike(payload);

        if (isLike) {
            marketLikeRepository.unLike(payload);
        } else {
            marketLikeRepository.like(payload);
        }

        return returnMap;
    }

    @Override
    public Map<String, Object> chargeCP(User user, BigDecimal amount) throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("user_id", user.getId());
            payload.put("account_type", AccountType.CP);
        }
        Account account = accountRepository.readAccount(payload);
        if (ObjectUtils.isEmpty(account))
            throw new ApiException(ApiErrorCode.ACCOUNT_NOT_FOUND);

        {
            payload.clear();
            payload.put("account_id", account.getId());
            payload.put("quantity", account.getQuantity().add(amount));
        }
        accountRepository.updateAccount(payload);

        {
            payload.clear();
            payload.put("to", account.getId());
            payload.put("title", "충전");
            payload.put("description", "CP 충전");
            payload.put("amount", amount);
        }
        accountRepository.createTransaction(payload);

        return returnMap;
    }

    @Override
    public Map<String, Object> readMarketInfo() throws Exception {
        Map<String, Object> returnMap = new HashMap<>();
        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("key", "market.");
        }
        List<Policy> policies = policyRepository.readPolicyList(payload);

        policies.forEach(item -> returnMap.put(item.getKey(), item.getValue()));

        return returnMap;
    }

}
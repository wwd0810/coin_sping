package com.laon.cashlink.common.lib;

import com.laon.cashlink.common.define.user.AccountType;
import com.laon.cashlink.entity.user.User;
import com.laon.cashlink.repository.user.AccountRepository;
import com.laon.cashlink.repository.user.UserRepository;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.oauth2.resource.PrincipalExtractor;
import org.springframework.stereotype.Component;

// @Slf4j
@Component
@RequiredArgsConstructor
public class CustomPrincipalExtractor implements PrincipalExtractor {

    private final UserRepository userRepository;
    private final AccountRepository accountRepository;

    @Override
    public Object extractPrincipal(Map<String, Object> map) {
        String principalId = (String) map.get("username");

        Map<String, Object> payload = new HashMap<>();
        {
            payload.put("username", principalId);
        }
        User user = userRepository.readUser(payload);

        if (user == null) {
            payload.clear();
            {
                payload.put("username", map.get("username"));
                payload.put("name", map.get("name"));
                payload.put("phone", map.get("phone"));
                payload.put("birth", map.get("birth"));
                payload.put("sex", map.get("sex"));
            }
            userRepository.createUser(payload);
            {
                payload.clear();
                payload.put("username", map.get("username"));
            }
            user = userRepository.readUser(payload);
            {
                payload.clear();
                payload.put("account_id", AccountLib.make());
                payload.put("account_type", AccountType.CP);
                payload.put("user_id", user.getId());
            }
            accountRepository.createAccount(payload);
            {
                payload.clear();
                payload.put("account_id", AccountLib.make());
                payload.put("account_type", AccountType.DL);
                payload.put("user_id", user.getId());
            }
            accountRepository.createAccount(payload);
        }

        return user;
    }

}

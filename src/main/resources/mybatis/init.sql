insert ignore into tbl_policy (`key`, `value`)
values ('market.condition', '100')
        , ('market.condition_upper', '110')
        , ('market.condition_lower', '90')
        , ('market.fees', '0.5')
        , ('market.sale.step', '1')
        , ('market.sale.min', '100')
        , ('market.sale.max', 'infinity')
        , ('market.sale.user_apply_limit', '10')
        , ('market.buy.user_apply_limit', '10')
        , ('application.android.version', '0.0.1')
        , ('application.android.require_version', '0.0.1')
        , ('application.ios.version', '0.0.1')
        , ('application.ios.require_version', '0.0.1');
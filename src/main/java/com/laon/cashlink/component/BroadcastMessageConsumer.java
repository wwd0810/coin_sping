package com.laon.cashlink.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BroadcastMessageConsumer {

    private static final String REMIT_DL_REQUEST_QUEUE = "remittance.dl.request.queue";
    // private static final String REMIT_DL_COMPLETE_QUEUE =
    // "remittance.dl.complete.queue";

    @RabbitListener(queues = { REMIT_DL_REQUEST_QUEUE })
    public void receiveMessageFromDirectExchangeWithRequestQueue(String message) {
        log.info("message: {}", message);
    }

}

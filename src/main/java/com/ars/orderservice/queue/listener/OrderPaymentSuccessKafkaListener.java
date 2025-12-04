package com.ars.orderservice.queue.listener;

import com.ars.orderservice.service.OrderService;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseKafkaConstants;
import com.dct.model.event.PaymentSuccessEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class OrderPaymentSuccessKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(OrderPaymentSuccessKafkaListener.class);
    private final OrderService orderService;

    public OrderPaymentSuccessKafkaListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
        topics = BaseKafkaConstants.Topic.REGISTER_SHOP,
        groupId = BaseKafkaConstants.GroupId.REGISTER_SHOP,
        concurrency = BaseKafkaConstants.Consumers.REGISTER_SHOP
    )
    public void receiveMessage(
        @Payload String payload,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String ignoredKey,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int ignoredPartition,
        Acknowledgment ack
    ) {
        log.info("[HANDLE_ORDER_COMPLETION_EVENT] - message payload: {}", payload);

        if (Objects.isNull(payload)) {
            log.error("[HANDLE_ORDER_COMPLETION_EVENT_FAILED] - message payload is null");
            ack.acknowledge();
            return;
        }

        try {
            PaymentSuccessEvent paymentSuccessEvent = JsonUtils.parseJson(payload, PaymentSuccessEvent.class);

            if (Objects.isNull(paymentSuccessEvent) || Objects.isNull(paymentSuccessEvent.getOrderId())) {
                log.error("[HANDLE_ORDER_COMPLETION_EVENT_FAILED] - event content or orderId is null");
                ack.acknowledge();
                return;
            }

            orderService.orderCompletion(paymentSuccessEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[HANDLE_ORDER_COMPLETION_EXCEPTION] - error: {}. Retry later", e.getMessage(), e);
        }
    }
}

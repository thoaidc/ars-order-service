package com.ars.orderservice.queue.listener;

import com.ars.orderservice.service.OrderService;
import com.dct.model.common.JsonUtils;
import com.dct.model.constants.BaseKafkaConstants;
import com.dct.model.event.PaymentFailureEvent;

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
public class OrderPaymentFailureKafkaListener {
    private static final Logger log = LoggerFactory.getLogger(OrderPaymentFailureKafkaListener.class);
    private final OrderService orderService;

    public OrderPaymentFailureKafkaListener(OrderService orderService) {
        this.orderService = orderService;
    }

    @KafkaListener(
        topics = BaseKafkaConstants.Topic.ORDER_PAYMENT_FAILURE,
        groupId = BaseKafkaConstants.GroupId.ORDER_PAYMENT_FAILURE,
        concurrency = BaseKafkaConstants.Consumers.ORDER_PAYMENT_FAILURE
    )
    public void receiveMessage(
        @Payload String payload,
        @Header(name = KafkaHeaders.RECEIVED_KEY, required = false) String ignoredKey,
        @Header(KafkaHeaders.RECEIVED_PARTITION) int ignoredPartition,
        Acknowledgment ack
    ) {
        log.info("[HANDLE_ROLLBACK_ORDER_EVENT] - message payload: {}", payload);

        if (Objects.isNull(payload)) {
            log.error("[HANDLE_ROLLBACK_ORDER_FAILED] - message payload is null");
            ack.acknowledge();
            return;
        }

        try {
            PaymentFailureEvent paymentFailureEvent = JsonUtils.parseJson(payload, PaymentFailureEvent.class);

            if (Objects.isNull(paymentFailureEvent) || Objects.isNull(paymentFailureEvent.getOrderId())) {
                log.error("[HANDLE_ROLLBACK_ORDER_FAILED] - event content or orderId is null");
                ack.acknowledge();
                return;
            }

            orderService.cancelOrder(paymentFailureEvent);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("[HANDLE_ROLLBACK_ORDER_EXCEPTION] - error. {}. Retry later", e.getMessage(), e);
        }
    }
}

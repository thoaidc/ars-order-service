package com.ars.orderservice.queue.publisher;

import com.dct.model.common.JsonUtils;
import com.dct.model.config.properties.KafkaProperties;
import com.dct.model.event.ChangeBalanceAmountEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@ConditionalOnClass(KafkaTemplate.class)
public class KafkaProducer {
    private final Logger log = LoggerFactory.getLogger(KafkaProducer.class);
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final KafkaProperties kafkaProperties;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, KafkaProperties kafkaProperties) {
        this.kafkaTemplate = kafkaTemplate;
        this.kafkaProperties = kafkaProperties;
    }

    public void sendMessageOrderCreated(String event) {
        log.info("[SEND_ORDER_CREATED_TOPIC] - {}", event);
        kafkaTemplate.send(kafkaProperties.getTopics().getOrderCreated(), event);
    }

    public void sendMessageChangeBalanceAmount(String event) {
        log.info("[SEND_CHANGE_BALANCE_AMOUNT_TOPIC] - {}", event);
        ChangeBalanceAmountEvent changeBalanceAmountEvent = JsonUtils.parseJson(event, ChangeBalanceAmountEvent.class);
        String partitionKey = String.valueOf(changeBalanceAmountEvent.getReceiverId());
        kafkaTemplate.send(kafkaProperties.getTopics().getChangeBalanceAmount(), partitionKey, event);
    }
}

package com.ars.orderservice.service.impl;

import com.ars.orderservice.entity.OutBox;
import com.ars.orderservice.queue.publisher.KafkaProducer;
import com.ars.orderservice.repository.OutBoxRepository;
import com.ars.orderservice.service.OutBoxService;
import com.dct.model.constants.BaseOutBoxConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class OutBoxServiceImpl implements OutBoxService {
    private final KafkaProducer kafkaProducer;
    private final OutBoxRepository outBoxRepository;
    private static final Logger log = LoggerFactory.getLogger(OutBoxServiceImpl.class);

    public OutBoxServiceImpl(KafkaProducer kafkaProducer, OutBoxRepository outBoxRepository) {
        this.kafkaProducer = kafkaProducer;
        this.outBoxRepository = outBoxRepository;
    }

    @Override
    @Transactional
    public void processOutBoxEvent() {
        List<OutBox> outBoxes = outBoxRepository.findTopOutBoxesByTypeAndStatus(
            BaseOutBoxConstants.Type.ORDER_CREATED,
            BaseOutBoxConstants.Status.PENDING
        );

        for (OutBox outBox : outBoxes) {
            if (Objects.nonNull(outBox)) {
                log.info("[SEND_EVENT_FROM_OUTBOX] - sagaId: {}, type: {}, content: {}",
                    outBox.getSagaId(), outBox.getType(), outBox.getValue()
                );
                kafkaProducer.sendMessageOrderCreated(outBox.getValue());
                outBox.setStatus(BaseOutBoxConstants.Status.COMPLETION);
            }
        }

        outBoxRepository.saveAll(outBoxes);
    }
}

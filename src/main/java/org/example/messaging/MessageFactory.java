package org.example.messaging;

import io.vertx.core.buffer.Buffer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.example.messaging.dto.MoneyTransferMessage;

/**
 * Created by Oleg Meleshin on 15.05.2018.
 */
public interface MessageFactory {

  KafkaProducerRecord<Buffer, Buffer> moneyTransferMessage(String topicName, Long key, MoneyTransferMessage message);

}

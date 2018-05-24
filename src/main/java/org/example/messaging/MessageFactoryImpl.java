package org.example.messaging;

import io.vertx.core.buffer.Buffer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import org.example.messaging.dto.MoneyTransferMessage;

/**
 * Created by Oleg Meleshin on 15.05.2018.
 */
public class MessageFactoryImpl implements MessageFactory {

  @Override
  public KafkaProducerRecord<Buffer, Buffer> moneyTransferMessage(String topicName, Long key, MoneyTransferMessage message) {
    Buffer keyBuffer = Buffer.buffer().appendLong(key);
    Buffer valueBuffer = Buffer.buffer()
        .appendLong(message.getId())
        .appendLong(message.getWithdrawClientId())
        .appendLong(message.getDepositClientId())
        .appendInt(message.getAmount());

    return KafkaProducerRecord.create(topicName, keyBuffer, valueBuffer);
  }

}

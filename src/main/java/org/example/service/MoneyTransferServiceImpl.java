package org.example.service;

import com.google.inject.Inject;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.kafka.client.producer.KafkaProducer;
import io.vertx.kafka.client.producer.KafkaProducerRecord;
import io.vertx.kafka.client.producer.RecordMetadata;
import org.example.app.Configuration;
import org.example.exceptions.ServiceException;
import org.example.messaging.MessageFactory;
import org.example.messaging.dto.MoneyTransferMessage;
import org.example.model.MoneyTransferRequest;
import org.example.model.MoneyTransferResponse;
import org.example.repositories.ClientRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Function;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class MoneyTransferServiceImpl implements MoneyTransferService {

  private static Logger logger = LoggerFactory.getLogger(MoneyTransferServiceImpl.class);

  private Configuration config;

  private ClientRepository clientRepository;

  private MessageFactory messageFactory;

  private KafkaProducer kafkaProducer;

  private Map<Long, ReentrantLock> clientLocks = new HashMap();

  private static AtomicLong moneyTransferId = new AtomicLong();

  @Inject
  public MoneyTransferServiceImpl(Configuration config, MessageFactory messageFactory, ClientRepository clientRepository, KafkaProducer kafkaProducer) {
    this.config = config;
    this.messageFactory = messageFactory;
    this.clientRepository = clientRepository;
    this.kafkaProducer = kafkaProducer;
  }

  @Override
  public MoneyTransferResponse transfer(MoneyTransferRequest request) {
    // Check if withdraw client id contained in client records
    Long withdrawClientId = request.getWithdrawClientId();
    if (!clientRepository.hasClient(withdrawClientId)) {
      throwServiceException(String.format("Withdraw client with such id (%d) is not exist", withdrawClientId));
    }

    // Check if deposit client id contained in client records
    Long depositClientId = request.getDepositClientId();
    if (!clientRepository.hasClient(depositClientId)) {
      throwServiceException(String.format("Deposit client with such id (%d) is not exist", depositClientId));
    }

    return deadlockSafe(request, (rq) -> doTransfer(rq.getWithdrawClientId(), rq.getDepositClientId(), rq.getAmount()));
  }

  @Override
  public int getAmount(long clientId) {
    return clientRepository.getAmount(clientId);
  }

  private MoneyTransferResponse deadlockSafe(MoneyTransferRequest request, Function<MoneyTransferRequest, MoneyTransferResponse> operation) {
    long withdrawClientId = request.getWithdrawClientId();
    long depositClientId = request.getDepositClientId();

    long firstId = Math.min(withdrawClientId, depositClientId);
    long secondId = Math.max(withdrawClientId, depositClientId);
    ReentrantLock firstLock = clientLocks.getOrDefault(firstId, new ReentrantLock());
    ReentrantLock secondLock = clientLocks.getOrDefault(secondId, new ReentrantLock());
    MoneyTransferResponse response = null;

    // Perform the ordered lock of two client records, so no any other thread can execute transfer simultaneosly
    // Ordered acquisition of locks is for mitigation of deadlock case
    try {
      int tries = 0;

      while (!firstLock.tryLock(1000, TimeUnit.MILLISECONDS)) {
        if (tries++ > 10) {
          throwServiceException(String.format("Unable to continue money transfer due %s client account (%d) lock. Try again later.",
              firstId == withdrawClientId? "withdraw": "deposit", firstId));
        }
      }

      while (!secondLock.tryLock(1000, TimeUnit.MILLISECONDS)) {
        if (tries++ > 10) {
          throwServiceException(String.format("Unable to continue money transfer due %s client account (%d) lock. Try again later.",
              firstId == withdrawClientId? "withdraw": "deposit", secondId));
        };
      }

      // perform main business operation - get the given amount of money from client A and transfer this to client B
      response = operation.apply(request);
    } catch (InterruptedException e) {
      throwServiceException("Unable to continue money transfer. Try again later", e);
    } finally {
      // release locks in reverse order
      if (secondLock != null && secondLock.isHeldByCurrentThread()) secondLock.unlock();
      if (firstLock != null && firstLock.isHeldByCurrentThread()) firstLock.unlock();
    }

    return response;
  }

  private MoneyTransferResponse doTransfer(long withdrawClientId, long depositClientId, int amount) {
    int withdrawAmount = clientRepository.getAmount(withdrawClientId);
    int depositAmount = clientRepository.getAmount(depositClientId);

    if (withdrawAmount - amount < 0)
      throwServiceException(String.format("Unable to withdraw amount '%d', which exceeded current client balance!", amount));

    clientRepository.setAmount(withdrawClientId, withdrawAmount - amount);
    clientRepository.setAmount(depositClientId, depositAmount + amount);

    if (logger.isInfoEnabled())
      logger.info("Transfer: ({}) -> ({}), amount: ({})", withdrawClientId, depositClientId, amount);

    Long currentTransferId = moneyTransferId.incrementAndGet();

    KafkaProducerRecord<Buffer, Buffer> record = messageFactory.moneyTransferMessage(config.getTopicName(),
        currentTransferId, new MoneyTransferMessage(currentTransferId, withdrawClientId, depositClientId, amount));

    kafkaProducer.write(record, new Handler<AsyncResult<RecordMetadata>>() {
      @Override
      public void handle(AsyncResult<RecordMetadata> event) {
        RecordMetadata recordMetadata = event.result();
        if (event.succeeded()) {
          logger.info("Transfer {} written on topic={}, partition={}, offset={}", moneyTransferId,
              recordMetadata.getTopic(), recordMetadata.getPartition(), recordMetadata.getOffset());
        } else {
          logger.info("Fail to write transfer {} on topic={}", moneyTransferId, recordMetadata.getTopic());
        }
      }
    });

    return new MoneyTransferResponse(currentTransferId, amount);
  }

  private void throwServiceException(String message) {
    logger.info(message);
    throw new ServiceException(message);
  }

  private void throwServiceException(String message, Throwable t) {
    logger.info(message, t);
    throw new ServiceException(message);
  }

}

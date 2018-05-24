package org.example.app;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.kafka.client.producer.KafkaProducer;
import org.example.controllers.MoneyTransferController;
import org.example.messaging.MessageFactory;
import org.example.messaging.MessageFactoryImpl;
import org.example.repositories.ClientRepository;
import org.example.repositories.ClientRepositoryImpl;
import org.example.service.MoneyTransferService;
import org.example.service.MoneyTransferServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 09.05.2018.
 */
public class AppModule extends AbstractModule {

  private static Logger logger = LoggerFactory.getLogger(AppModule.class);

  private Vertx vertx;

  private EventBus eventBus;

  public AppModule(Vertx vertx, EventBus eventBus) {
    this.vertx = vertx;
    this.eventBus = eventBus;
  }

  @Override
  protected void configure() {
    bind(Configuration.class).asEagerSingleton();
    bind(MessageFactory.class).to(MessageFactoryImpl.class).asEagerSingleton();
    bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class).asEagerSingleton();
    bind(MoneyTransferController.class).asEagerSingleton();
  }

  @Provides
  public ClientRepository clientRepository() {
    Map<Long, Integer> clientAmounts = new HashMap();
    clientAmounts.put(12L, 100);
    clientAmounts.put(13L, 100);
    clientAmounts.put(14L, 100);
    clientAmounts.put(15L, 100);

    return new ClientRepositoryImpl(clientAmounts);
  }

  @Provides
  @Inject
  public KafkaProducer kafkaProducer(Configuration config) {
    KafkaProducer kafkaProducer = KafkaProducer.create(vertx, config.getKafkaProducerConfig(), Buffer.class, Buffer.class);
    kafkaProducer.exceptionHandler(new Handler<Throwable>() {
      @Override
      public void handle(Throwable event) {
        logger.error("Error in kafka producer : " + event.getMessage(), event.getCause());
      }
    });

    return kafkaProducer;
  }

  @Provides
  public EventBus eventBus(){
    return eventBus;
  }

  @Provides
  public Vertx vertx() {
    return vertx;
  }
}

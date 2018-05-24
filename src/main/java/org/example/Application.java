package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import org.example.app.Configuration;
import org.example.app.AppModule;
import org.example.app.Routes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Collectors;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class Application {

  private static Logger logger = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    Vertx vertx = Vertx.vertx();
    Injector injector = Guice.createInjector(new AppModule(vertx, vertx.eventBus()));

    Configuration configuration = injector.getInstance(Configuration.class);

    if (logger.isInfoEnabled()) {
      String kafkaProducerConfig = configuration.getKafkaProducerConfig()
          .entrySet().stream().map(e -> e.getKey() + ":" + e.getValue()).collect(Collectors.joining(", "));
      logger.info("Kafka producer config: {}", kafkaProducerConfig);
    }

    HttpServer server = vertx.createHttpServer();
    Router router = Router.router(vertx);

    int port = configuration.getPort();
    logger.info("Start service on port {} ...", port);
    new Routes(injector, router).init();

    server.requestHandler(router::accept).listen(port);
  }

}

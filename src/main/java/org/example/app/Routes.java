package org.example.app;

import com.google.inject.Injector;
import io.vertx.core.Handler;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.BodyHandler;
import org.example.controllers.MoneyTransferController;
import org.example.exceptions.RequestValidationException;

import java.util.function.Function;

import static org.example.Json.failureResponse;
import static org.example.Json.okResponse;

/**
 * Created on 09.05.2018.
 */
public class Routes {

  private Injector injector;

  private Router router;

  public Routes(Injector injector, Router router) {
    this.injector = injector;
    this.router = router;
  }

  public void init() {
    router.route()
        .handler(BodyHandler.create())
        .failureHandler(failureRoutingContext -> {

          Throwable t = failureRoutingContext.failure();
          HttpServerResponse response = failureRoutingContext.response();

          if (t instanceof RequestValidationException) {
            response.setStatusCode(400);
          } else {
            response.setStatusCode(500);
          }

          response.end(failureResponse(t.getMessage()).encode());
        });

    MoneyTransferController moneyTransferController = injector.getInstance(MoneyTransferController.class);
    router.post("/api/v1/money/transfer/")
        .consumes("application/json")
        .produces("application/json")
        .blockingHandler(restHandler(routingContext -> moneyTransferController.transferMoney(routingContext)));
  }

  private <T> Handler<RoutingContext> restHandler(Function<RoutingContext, T> responseSupplier) {
    return routingContext -> {
      routingContext.response().end(okResponse(responseSupplier.apply(routingContext)).encode());
    };
  }

}

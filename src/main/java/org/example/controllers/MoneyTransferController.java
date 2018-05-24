package org.example.controllers;

import com.google.inject.Inject;
import io.vertx.ext.web.RoutingContext;
import org.example.QueryParamsHelper;
import org.example.model.MoneyTransferRequest;
import org.example.model.MoneyTransferResponse;
import org.example.service.MoneyTransferService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Oleg Meleshin on 08.05.2018.
 */
public class MoneyTransferController {

  private static Logger logger = LoggerFactory.getLogger(MoneyTransferController.class);

  private MoneyTransferService moneyTransferService;

  @Inject
  public MoneyTransferController(MoneyTransferService moneyTransferService) {
    this.moneyTransferService = moneyTransferService;
  }

  public MoneyTransferResponse transferMoney(RoutingContext routingContext) {
    MoneyTransferRequest request = null;

    try {
      request = new QueryParamsHelper().parseParams(routingContext.getBodyAsJson());
    } catch (Exception e) {
      routingContext.fail(e);
    }

    logger.info("Start processing request {}", request);

    return moneyTransferService.transfer(request);
  }
}

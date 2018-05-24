package org.example;

import io.vertx.core.json.JsonObject;
import org.example.exceptions.RequestValidationException;
import org.example.model.MoneyTransferRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class QueryParamsHelper {

  private static Logger logger = LoggerFactory.getLogger(QueryParamsHelper.class);

  public MoneyTransferRequest parseParams(JsonObject body) {
    MoneyTransferRequest request = new MoneyTransferRequest();

    Long withdrawClientId = parseLongParam(body, "withdrawClientId");
    if (withdrawClientId < 0) {
      throwValidationException("'withdrawClientId' can't be a negative number");
    }

    request.setWithdrawClientId(withdrawClientId);

    Long depositClientId = parseLongParam(body, "depositClientId");
    if (depositClientId < 0)
      throwValidationException("'depositClientId' can't be a negative number");

    request.setDepositClientId(depositClientId);

    if (depositClientId == withdrawClientId) {
      throwValidationException("Client can't transfer money to himself/herself");
    }

    Integer amount = parseIntParam(body, "amount");
    if (amount <= 0) {
      throwValidationException("Clients can't transfer zero or negative amounts!");
    }
    request.setAmount(amount);

    return request;
  }

  private Long parseLongParam(JsonObject body, String paramName) {
    Long value = null;
    try {
      value = body.getLong(paramName);
    } catch (Exception e) {
      throwValidationException(String.format("Parameter '%s' of no 'long' type!", paramName), e);
    }

    if (value == null) {
      throwValidationException(String.format("Parameter '%s' is missed in request", paramName));
    }

    return value;
  }

  private Integer parseIntParam(JsonObject body, String paramName) {
    Integer value = null;
    try {
      value = body.getInteger(paramName);
    } catch (Exception e) {
      throwValidationException(String.format("Parameter '%s' of no 'int' type!", paramName), e);
    }

    if (value == null) {
      throwValidationException(String.format("Parameter '%s' is missed in request", paramName));
    }

    return value;
  }

  private void throwValidationException(String message) {
    logger.error(message);
    throw new RequestValidationException(message);
  }

  private void throwValidationException(String message, Throwable throwable) {
    logger.error(message, throwable);
    throw new RequestValidationException(message);
  }
}

package org.example;

import io.vertx.core.json.JsonObject;
import org.example.model.MoneyTransferResponse;

/**
 * Created by Oleg Meleshin on 08.05.2018.
 */
public class Json {

  public static JsonObject requestAsJson(Object withdrawClientId, Object depositClientId, Object amount) {
    return new JsonObject()
        .put("withdrawClientId", withdrawClientId)
        .put("depositClientId", depositClientId)
        .put("amount", amount);
  }

  public static <T> JsonObject okResponse(T response) {
    return new JsonObject().put("success", true).put("data", JsonObject.mapFrom(response));
  }

  public static JsonObject failureResponse(String message) {
    return new JsonObject().put("success", false).put("message", message);
  }
}

package org.example;

import org.junit.Test;

import static org.example.Json.requestAsJson;
import static org.example.TestUtils.expectValidationException;

/**
 * Created by Oleg Meleshin on 08.05.2018.
 */
public class QueryParamsHelperTest {

  private QueryParamsHelper paramsHelper = new QueryParamsHelper();

  @Test
  public void testClientIdsAreNegative() {
    expectValidationException("'withdrawClientId' can't be a negative number",
        () -> paramsHelper.parseParams(requestAsJson(-12L, 13L, 100)));
    expectValidationException("'depositClientId' can't be a negative number",
        () -> paramsHelper.parseParams(requestAsJson(12L, -13L, 100)));
  }

  @Test
  public void clientIdsOfInvalidTypes() {
    expectValidationException("Parameter 'withdrawClientId' of no 'long' type!",
        () -> paramsHelper.parseParams(requestAsJson("clientA", 13L, 100)));
    expectValidationException("Parameter 'depositClientId' of no 'long' type!",
        () -> paramsHelper.parseParams(requestAsJson(12L, "clientB", 100)));
  }

  @Test
  public void clientIdsAreMissed() {
    expectValidationException("Parameter 'withdrawClientId' is missed in request",
        () -> paramsHelper.parseParams(requestAsJson(null, 13L, 100)));
    expectValidationException("Parameter 'depositClientId' is missed in request",
        () -> paramsHelper.parseParams(requestAsJson(12L, null, 100)));
  }

  @Test
  public void clientIdsAreMatch() {
    expectValidationException("Client can't transfer money to himself/herself",
        () -> paramsHelper.parseParams(requestAsJson(13L, 13L, 100)));
  }

  @Test
  public void amountIsNegativeOrZero() {
    expectValidationException("Clients can't transfer zero or negative amounts!",
        () -> paramsHelper.parseParams(requestAsJson(12L, 13L, -100)));
    expectValidationException("Clients can't transfer zero or negative amounts!",
        () -> paramsHelper.parseParams(requestAsJson(12L, 13L, 0)));
  }

  @Test
  public void amountIsMissed() {
    expectValidationException("Parameter 'amount' is missed in request",
        () -> paramsHelper.parseParams(requestAsJson(12L, 13L, null)));
  }

}

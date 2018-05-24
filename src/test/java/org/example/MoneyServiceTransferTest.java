package org.example;

import com.google.inject.Guice;
import com.google.inject.Injector;
import io.vertx.core.Vertx;
import org.example.app.AppModule;
import org.example.model.MoneyTransferRequest;
import org.example.repositories.ClientRepository;
import org.example.service.MoneyTransferService;
import org.example.service.MoneyTransferServiceImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.util.concurrent.CompletableFuture.allOf;
import static java.util.concurrent.CompletableFuture.runAsync;
import static org.example.TestUtils.expectServiceException;
import static org.junit.Assert.assertEquals;

/**
 * Created by Oleg Meleshin on 08.05.2018.
 */
public class MoneyServiceTransferTest {

  private MoneyTransferService service;

  @Before
  public void init() {
    Vertx vertx = Vertx.vertx();
    Injector injector = Guice.createInjector(new AppModule(vertx, vertx.eventBus()));
    this.service = injector.getInstance(MoneyTransferService.class);
  }

  @Test
  public void testMoneyTransfer() {
    assertEquals(100, (int) service.getAmount(12L));
    assertEquals(100, (int) service.getAmount(13L));
    service.transfer(new MoneyTransferRequest(12L, 13L, 100));
    assertEquals(0, (int) service.getAmount(12L));
    assertEquals(200, (int) service.getAmount(13L));
  }

  @Test
  public void testTransferByNonExistingIds() {
    expectServiceException("Withdraw client with such id (22) is not exist",
        () -> service.transfer(new MoneyTransferRequest(22L, 13L, 100)));

    expectServiceException("Deposit client with such id (23) is not exist",
        () -> service.transfer(new MoneyTransferRequest(12L, 23L, 100)));
  }

  @Test
  public void testTryToTransferExceededAmount() {
    expectServiceException("Unable to withdraw amount '200', which exceeded current client balance!",
        () -> service.transfer(new MoneyTransferRequest(12L, 13L, 200)));
  }

  @Test
  public void testConcurrentTransfer() {
    assertEquals(100, (int) service.getAmount(12L));
    assertEquals(100, (int) service.getAmount(13L));

    ExecutorService executorService = Executors.newFixedThreadPool(2);
    allOf(
      runAsync(() -> service.transfer(new MoneyTransferRequest(12L, 13L, 100)), executorService),
      runAsync(() -> service.transfer(new MoneyTransferRequest(12L, 13L, 100)), executorService)
    ).thenRun(() -> {
      assertEquals(100, (int) service.getAmount(12L));
      assertEquals(100, (int) service.getAmount(13L));
    });
  }
}

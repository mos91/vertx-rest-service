package org.example;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import org.example.repositories.ClientRepository;
import org.example.repositories.ClientRepositoryImpl;
import org.example.service.MoneyTransferService;
import org.example.service.MoneyTransferServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 09.05.2018.
 */
public class TestModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(MoneyTransferService.class).to(MoneyTransferServiceImpl.class).asEagerSingleton();
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
}

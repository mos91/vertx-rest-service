package org.example.repositories;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 09.05.2018.
 */
public class ClientRepositoryImpl implements ClientRepository {

  private static Logger logger = LoggerFactory.getLogger(ClientRepositoryImpl.class);

  private Map<Long, Integer> amounts = new HashMap();

  public ClientRepositoryImpl(Map<Long, Integer> amounts) {
    this.amounts = amounts;
  }

  @Override
  public boolean hasClient(long clientId) {
    return amounts.containsKey(clientId);
  }

  @Override
  public Integer getAmount(long clientId) {
    return amounts.get(clientId);
  }

  @Override
  public void setAmount(long clientId, int amount) {
    this.amounts.put(clientId, amount);
  }
}

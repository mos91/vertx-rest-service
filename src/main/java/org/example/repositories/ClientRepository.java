package org.example.repositories;

/**
 * Created on 09.05.2018.
 */
public interface ClientRepository {

  boolean hasClient(long clientId);

  Integer getAmount(long clientId);

  void setAmount(long clientId, int amount);
}

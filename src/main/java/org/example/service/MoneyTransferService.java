package org.example.service;

import org.example.model.MoneyTransferRequest;
import org.example.model.MoneyTransferResponse;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public interface MoneyTransferService {

  MoneyTransferResponse transfer(MoneyTransferRequest request);

  int getAmount(long clientId);
}

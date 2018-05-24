package org.example.model;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class MoneyTransferResponse {

  private long id;

  private int amount;

  public MoneyTransferResponse(long id, int amount) {
    this.id = id;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public int getAmount() {
    return amount;
  }

  public void setAmount(int amount) {
    this.amount = amount;
  }

}

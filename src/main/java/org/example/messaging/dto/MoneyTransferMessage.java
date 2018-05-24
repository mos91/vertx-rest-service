package org.example.messaging.dto;

import java.io.Serializable;

/**
 * Created by Oleg Meleshin on 11.05.2018.
 */
public class MoneyTransferMessage implements Serializable {

  private long id;

  private Long withdrawClientId;

  private Long depositClientId;

  private Integer amount;

  public MoneyTransferMessage(long id, Long withdrawClientId, Long depositClientId, Integer amount) {
    this.id = id;
    this.withdrawClientId = withdrawClientId;
    this.depositClientId = depositClientId;
    this.amount = amount;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Long getWithdrawClientId() {
    return withdrawClientId;
  }

  public void setWithdrawClientId(Long withdrawClientId) {
    this.withdrawClientId = withdrawClientId;
  }

  public Long getDepositClientId() {
    return depositClientId;
  }

  public void setDepositClientId(Long depositClientId) {
    this.depositClientId = depositClientId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "MoneyTransferMessage{" +
        "id=" + id +
        ", withdrawClientId=" + withdrawClientId +
        ", depositClientId=" + depositClientId +
        ", amount=" + amount +
        '}';
  }

}

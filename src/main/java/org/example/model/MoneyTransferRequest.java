package org.example.model;

/**
 * Created by Oleg Meleshin on 07.05.2018.
 */
public class MoneyTransferRequest {

  private Long withdrawClientId;

  private Long depositClientId;

  private Integer amount;

  public MoneyTransferRequest(Long withdrawClientId, Long depositClientId, Integer amount) {
    this.withdrawClientId = withdrawClientId;
    this.depositClientId = depositClientId;
    this.amount = amount;
  }

  public MoneyTransferRequest() {}

  public Long getDepositClientId() {
    return depositClientId;
  }

  public void setDepositClientId(Long depositClientId) {
    this.depositClientId = depositClientId;
  }

  public Long getWithdrawClientId() {
    return withdrawClientId;
  }

  public void setWithdrawClientId(Long withdrawClientId) {
    this.withdrawClientId = withdrawClientId;
  }

  public Integer getAmount() {
    return amount;
  }

  public void setAmount(Integer amount) {
    this.amount = amount;
  }

  @Override
  public String toString() {
    return "MoneyTransferRequest{" +
        "withdrawClientId=" + withdrawClientId +
        ", depositClientId=" + depositClientId +
        ", amount=" + amount +
        '}';
  }

}

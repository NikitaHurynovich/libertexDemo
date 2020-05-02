package com.nikitahurynovich.LibertexDemo.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionOutput {
  private Long oldBalance;
  private Long newBalance;
  private TransactionInput transactionInput;

  public TransactionOutput(TransactionInput transactionInput, Long newBalance, Long oldBalance) {
    this.newBalance = newBalance;
    this.oldBalance = oldBalance;
    this.transactionInput = transactionInput;
  }
}

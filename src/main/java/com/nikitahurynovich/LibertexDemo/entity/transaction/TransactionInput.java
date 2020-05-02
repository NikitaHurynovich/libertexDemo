package com.nikitahurynovich.LibertexDemo.entity.transaction;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionInput {
  private Long walletId;
  private Long value;
}

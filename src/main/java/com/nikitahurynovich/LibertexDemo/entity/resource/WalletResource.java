package com.nikitahurynovich.LibertexDemo.entity.resource;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WalletResource {
  private Long id;
  private Long balance;

  public WalletResource(WalletType walletType) {
    this.id = walletType.getId();
    this.balance = walletType.getBalance();
  }
}

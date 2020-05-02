package com.nikitahurynovich.LibertexDemo.controller;

import java.util.Optional;
import javax.swing.text.html.Option;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.entity.resource.WalletResource;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.service.WalletService;

@RestController
public class WalletController {

  private final WalletService walletService;

  @Autowired
  public WalletController(WalletService walletService) {
    this.walletService = walletService;
  }

  @GetMapping("/api/wallet")
  public ResponseEntity<WalletResource> getWalletBalance(@RequestParam("walletId") Long walletId) {
    Optional<WalletType> wallet = walletService.getBalance(walletId);
    if (!wallet.isPresent()) {
      return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(new WalletResource(wallet.get()));
  }

  @PostMapping("/api/wallet/transaction")
  public ResponseEntity addTransaction(@RequestBody TransactionInput transactionInput) {
    walletService.sendTransaction(transactionInput);
    return ResponseEntity.accepted().build();
  }

}

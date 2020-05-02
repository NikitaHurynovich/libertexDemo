package com.nikitahurynovich.LibertexDemo.service;

import java.util.Optional;

import org.springframework.kafka.annotation.KafkaListener;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;

public interface WalletService {
  boolean sendTransaction(TransactionInput transactionMessage);
  Optional<WalletType> getBalance(Long walletId);

  @KafkaListener(topics = "${kafka.transactions.topic.name}", containerFactory = "transactionKafkaListenerContainerFactory")
  void transactionListener(TransactionInput transaction);
}

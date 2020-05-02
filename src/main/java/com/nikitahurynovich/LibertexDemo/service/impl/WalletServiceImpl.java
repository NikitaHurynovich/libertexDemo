package com.nikitahurynovich.LibertexDemo.service.impl;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionOutput;
import com.nikitahurynovich.LibertexDemo.repository.WalletRepository;
import com.nikitahurynovich.LibertexDemo.service.WalletService;

@Service
public class WalletServiceImpl implements WalletService {
  private final static Logger LOGGER = LoggerFactory.getLogger(WalletServiceImpl.class);

  @Value(value = "${kafka.transactions.topic.name}")
  private String transactionTopicName;

  @Value(value = "${kafka.transactions.dlq.topic.name}")
  private String transactionDLQTopicName;

  @Value("${kafka.wallet.topic.name}")
  private String walletTopicName;

  private final KafkaTemplate<String, TransactionInput> transactionKafkaTemplate;
  private final KafkaTemplate<String, TransactionOutput> walletKafkaTemplate;
  private final WalletRepository walletRepository;

  @Autowired
  public WalletServiceImpl(
      KafkaTemplate<String, TransactionInput> transactionKafkaTemplate,
      KafkaTemplate<String, TransactionOutput> walletKafkaTemplate,
      WalletRepository walletRepository) {
    this.transactionKafkaTemplate = transactionKafkaTemplate;
    this.walletKafkaTemplate = walletKafkaTemplate;
    this.walletRepository = walletRepository;
  }

  @Override
  public void transactionListener(TransactionInput transaction) {
    Optional<WalletType> walletType = walletRepository.findById(transaction.getWalletId());
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      if (!walletType.isPresent()) {
        LOGGER.error("cant find wallet for transaction {}", objectMapper.writeValueAsString(transaction));
        return;
      }
      Long oldBalance = walletType.get().getBalance();
      Long newBalance = oldBalance + transaction.getValue();
      if (newBalance < 0) {
        LOGGER.warn("Balance can not be < 0, transaction = {}", objectMapper.writeValueAsString(transaction));
        transactionKafkaTemplate.send(transactionDLQTopicName, transaction.getWalletId().toString(), transaction);
        return;
      }
      walletType.get().setBalance(newBalance);
      walletRepository.save(walletType.get());
      walletKafkaTemplate.send(walletTopicName, transaction.getWalletId().toString(),
          new TransactionOutput(transaction, oldBalance, newBalance));

    } catch (Exception e) {
      transactionKafkaTemplate.send(transactionDLQTopicName,transaction.getWalletId().toString(), transaction);
      LOGGER.error("error while transaction processing", e);
    }
  }

  @Override
  public boolean sendTransaction(TransactionInput transactionMessage) {
    transactionKafkaTemplate.send(transactionTopicName,transactionMessage.getWalletId().toString(), transactionMessage);
    return Boolean.TRUE;
  }

  @Override
  public Optional<WalletType> getBalance(Long walletId) {
    return walletRepository.findById(walletId);
  }


}

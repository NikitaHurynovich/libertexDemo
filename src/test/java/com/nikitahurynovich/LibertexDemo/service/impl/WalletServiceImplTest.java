package com.nikitahurynovich.LibertexDemo.service.impl;

import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.*;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;

import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionOutput;
import com.nikitahurynovich.LibertexDemo.repository.WalletRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class WalletServiceImplTest {

  private KafkaTemplate<String, TransactionInput> transactionKafkaTemplate;
  private KafkaTemplate<String, TransactionOutput> walletKafkaTemplate;
  private WalletRepository walletRepository;
  private ListenableFuture<SendResult<String, TransactionInput>> inputResult;
  private ListenableFuture<SendResult<String, TransactionOutput>> outputResult;
  private WalletServiceImpl walletService;

  private TransactionInput transactionInput;
  private WalletType walletType;


  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
    transactionInput = new TransactionInput();
    walletType = new WalletType();
    transactionInput.setWalletId(1l);
    transactionInput.setValue(10l);
    walletType.setId(1l);
    walletType.setBalance(0l);
    transactionKafkaTemplate = Mockito.mock(KafkaTemplate.class);
    walletKafkaTemplate = Mockito.mock(KafkaTemplate.class);
    walletRepository = Mockito.mock(WalletRepository.class);
    inputResult = Mockito.mock(ListenableFuture.class);
    outputResult = Mockito.mock(ListenableFuture.class);
    walletService = new WalletServiceImpl(transactionKafkaTemplate, walletKafkaTemplate, walletRepository);
  }

  @After
  public void reset_mocks() {
    Mockito.reset(transactionKafkaTemplate, walletKafkaTemplate);
  }

  @Test
  public void transactionListenerOK() {
    transactionInput.setWalletId(1l);
    transactionInput.setValue(10l);
    walletType.setId(1l);
    walletType.setBalance(0l);
    when(walletRepository.findById(anyLong())).thenReturn(Optional.of(walletType));
    when(walletRepository.save(any(WalletType.class))).thenReturn(walletType);
    when(walletKafkaTemplate.send(anyString(), anyString(), any())).thenReturn(outputResult);

    walletService.transactionListener(transactionInput);
    ArgumentCaptor<WalletType> walletCaptor = ArgumentCaptor.forClass(WalletType.class);
    ArgumentCaptor<TransactionOutput> transactionOutputArgumentCaptor= ArgumentCaptor.forClass(TransactionOutput.class);
    ArgumentCaptor<String> stringArgumentCaptor= ArgumentCaptor.forClass(String.class);
    verify(walletRepository).save(walletCaptor.capture());
    verify(walletKafkaTemplate).send(stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), transactionOutputArgumentCaptor.capture());
    assertEquals(transactionInput.getValue(), walletCaptor.getValue().getBalance());
    assertEquals(transactionInput.getValue(), transactionOutputArgumentCaptor.getValue().getTransactionInput().getValue());
  }

  @Test
  public void transactionListenerDLQ() {
    transactionInput.setValue(-10l);
    walletType.setBalance(0l);
    when(walletRepository.findById(anyLong())).thenReturn(Optional.of(walletType));
    when(transactionKafkaTemplate.send(anyString(), anyString(), any())).thenReturn(inputResult);

    walletService.transactionListener(transactionInput);
    ArgumentCaptor<TransactionInput> inputArgumentCaptor = ArgumentCaptor.forClass(TransactionInput.class);
    ArgumentCaptor<String> stringArgumentCaptor= ArgumentCaptor.forClass(String.class);
    verify(transactionKafkaTemplate).send(stringArgumentCaptor.capture(), stringArgumentCaptor.capture(), inputArgumentCaptor.capture());
    assertEquals(transactionInput.getValue(), inputArgumentCaptor.getValue().getValue());
    assertNotEquals(walletType.getBalance(), inputArgumentCaptor.getValue().getValue());
  }

  @Test
  public void sendTransaction() {
    when(transactionKafkaTemplate.send(anyString(), anyString(), any())).thenReturn(inputResult);
    walletService.sendTransaction(transactionInput);
  }

}

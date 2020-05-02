package com.nikitahurynovich.LibertexDemo.controller;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.entity.transaction.TransactionInput;
import com.nikitahurynovich.LibertexDemo.service.WalletService;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(WalletController.class)
public class WalletControllerTest {

  private final Integer BALANCE = 10;

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  WalletService walletService;

  @Test
  public void getWalletBalance() throws Exception {
    WalletType walletType = new WalletType();
    walletType.setBalance((long)BALANCE);
    given(walletService.getBalance(anyLong())).willReturn(Optional.of(walletType));
    mockMvc.perform(get("/api/wallet?walletId=1")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance", is(BALANCE)));
  }

  @Test
  public void addTransaction() throws Exception {
    given(walletService.sendTransaction(ArgumentMatchers.any(TransactionInput.class))).willReturn(true);
    mockMvc.perform(post("/api/wallet/transaction").content("{" + "\"value\": 10," + "\"walletId\": 2" + "}")
        .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isAccepted());

  }
}

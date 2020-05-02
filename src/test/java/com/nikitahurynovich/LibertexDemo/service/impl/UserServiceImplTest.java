package com.nikitahurynovich.LibertexDemo.service.impl;

import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.nikitahurynovich.LibertexDemo.entity.db.UserType;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.repository.UserRepository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceImplTest {

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserServiceImpl userService;

  @Before
  public void setUp() throws Exception {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void initAdminUser() {
    when(userRepository.findByName(anyString())).thenReturn(null);
    WalletType walletType = new WalletType();
    walletType.setBalance(10l);
    when(userRepository.save(any(UserType.class))).thenReturn((new UserType(Arrays.asList(walletType), "admin")));
    userService.initAdminUser();
  }
}

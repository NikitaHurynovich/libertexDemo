package com.nikitahurynovich.LibertexDemo.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.nikitahurynovich.LibertexDemo.entity.db.UserType;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;
import com.nikitahurynovich.LibertexDemo.repository.UserRepository;
import com.nikitahurynovich.LibertexDemo.service.UserService;

@Service
public class UserServiceImpl implements UserService {

  private final String ADMIN_NAME = "admin";
  private final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

  @Value(value = "${spring.kafka.streams.bootstrap-servers}")
  private String kafkaAddress;

  private final UserRepository userRepository;
  public UserServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public Optional<UserType> findById(Long Id) {
    return userRepository.findById(Id);
  }

  @PostConstruct
  public void initAdminUser(){
    UserType admin = userRepository.findByName(ADMIN_NAME);
    if (admin != null && admin.getWallets() != null && admin.getWallets().size() > 0) {
      return;
    }
    admin = new UserType();
    admin.setName(ADMIN_NAME);
    List<WalletType> walletTypes = new ArrayList<>();
    walletTypes.add(new WalletType(admin));
    admin.setWallets(walletTypes);
    admin = userRepository.save(admin);
    LOGGER.info("Admin user with Id = {}, walletId = {}, init balance = {}", admin.getId(),
        admin.getWallets().get(0).getId(),admin.getWallets().get(0).getBalance());
    LOGGER.info("Kafka address is {}", kafkaAddress);
  }
}

package com.nikitahurynovich.LibertexDemo.service;

import java.util.Optional;

import com.nikitahurynovich.LibertexDemo.entity.db.UserType;

public interface UserService {
  public Optional<UserType> findById(Long Id);
}

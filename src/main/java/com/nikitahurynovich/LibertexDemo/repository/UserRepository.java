package com.nikitahurynovich.LibertexDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.nikitahurynovich.LibertexDemo.entity.db.UserType;

public interface UserRepository extends JpaRepository<UserType, Long> {

  UserType findByName(String name);
}

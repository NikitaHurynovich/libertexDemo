package com.nikitahurynovich.LibertexDemo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import com.nikitahurynovich.LibertexDemo.entity.db.UserType;
import com.nikitahurynovich.LibertexDemo.entity.db.WalletType;

public interface WalletRepository extends JpaRepository<WalletType, Long> {
  List<WalletType> findByOwner(UserType owner);

  @Query(
      value = "SELECT * FROM wallet_type w WHERE w.user_type_fk = ?1",
      nativeQuery = true)
  List<WalletType> findByOwnerId(Long userId);
}

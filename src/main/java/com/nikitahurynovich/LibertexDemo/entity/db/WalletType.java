package com.nikitahurynovich.LibertexDemo.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class WalletType extends BaseEntity {
  @Column
  private Long balance = 0l;

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "user_type_fk")
  private UserType owner;

  public WalletType(UserType owner) {
    this.owner= owner;
  }
}

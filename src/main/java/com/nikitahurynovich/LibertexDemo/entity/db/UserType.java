package com.nikitahurynovich.LibertexDemo.entity.db;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import javax.persistence.*;

@Entity
@Data
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserType extends BaseEntity {
  @OneToMany(mappedBy = "owner", fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
  private List<WalletType> wallets;
  @Column(unique = true)
  private String name;
}

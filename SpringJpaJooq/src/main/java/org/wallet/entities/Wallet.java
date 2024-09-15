package org.wallet.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import org.wallet.repositories.uuid.annotations.WalletUuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "wallet")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Wallet {

    @Id
    @WalletUuidGenerator
    private UUID uuid;

    @Column(nullable = false)
    @Check(constraints = "amount >= 0")
    private Long amount;
}

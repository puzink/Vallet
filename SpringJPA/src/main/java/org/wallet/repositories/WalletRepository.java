package org.wallet.repositories;

import org.springframework.data.repository.CrudRepository;
import org.wallet.entities.Wallet;

import java.util.UUID;

public interface WalletRepository extends CrudRepository<Wallet, UUID> {
}
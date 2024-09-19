package org.wallet.repositories;

import org.springframework.context.annotation.Profile;
import org.springframework.data.repository.CrudRepository;
import org.wallet.entities.Wallet;

import java.util.UUID;

@Profile("jpa")
public interface WalletRepository extends CrudRepository<Wallet, UUID> {
}
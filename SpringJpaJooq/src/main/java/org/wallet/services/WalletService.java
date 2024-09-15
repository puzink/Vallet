package org.wallet.services;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Transactional(isolation = Isolation.REPEATABLE_READ)
public interface WalletService {

    long withdraw(UUID uuid, long amount);
    long deposit(UUID uuid, long amount);

}

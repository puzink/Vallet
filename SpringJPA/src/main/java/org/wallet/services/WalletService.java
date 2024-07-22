package org.wallet.services;

import java.util.UUID;

public interface WalletService {

    long withdraw(UUID uuid, long amount);
    long deposit(UUID uuid, long amount);

}

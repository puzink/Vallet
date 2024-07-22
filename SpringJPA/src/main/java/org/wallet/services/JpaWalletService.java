package org.wallet.services;

import org.postgresql.util.PSQLException;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.entities.Wallet;
import org.wallet.model.NotEnoughFundsException;
import org.wallet.model.WalletNotFoundException;
import org.wallet.repositories.WalletRepository;

import java.util.UUID;

@Service
@Transactional(isolation = Isolation.REPEATABLE_READ)
@Retryable(retryFor = PSQLException.class)
public class JpaWalletService implements WalletService {

    private final WalletRepository walletRepository;

    public JpaWalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public long withdraw(UUID uuid, long amount) {
        Wallet wallet = walletRepository.findById(uuid)
                .orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + uuid));
        if (wallet.getAmount() < amount) {
            throw new NotEnoughFundsException("Not enough funds. Amount to withdraw: " + amount);
        }
        wallet.setAmount(wallet.getAmount() - amount);
        walletRepository.save(wallet);
        return wallet.getAmount();
    }

    @Override
    public long deposit(UUID uuid, long amount) {
        Wallet wallet = walletRepository.findById(uuid)
                .orElse(new Wallet(uuid, 0L));
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository.save(wallet);
        return wallet.getAmount();
    }
}

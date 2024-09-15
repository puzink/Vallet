package org.wallet.services;

import lombok.extern.slf4j.Slf4j;
import org.postgresql.util.PSQLException;
import org.springframework.context.annotation.Profile;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.wallet.entities.Wallet;
import org.wallet.model.NotEnoughFundsException;
import org.wallet.model.WalletNotFoundException;
import org.wallet.repositories.WalletRepository;

import java.util.Optional;
import java.util.UUID;

@Service
@Retryable(retryFor = PSQLException.class)
@Profile("jpa")
@Slf4j
public class JpaWalletService implements WalletService {

    private final WalletRepository walletRepository;

    public JpaWalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


    @Override
    public long withdraw(UUID uuid, long amount) {
        log.info("Try to withdraw...");
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
        log.info("Try to deposit...");
        Optional<Wallet> wallet0 = walletRepository.findById(uuid);
        if (wallet0.isEmpty()) {
            log.info("Create new wallet");
        }
        Wallet wallet = wallet0.orElse(new Wallet(uuid, 0L));
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository.save(wallet);
        return wallet.getAmount();
    }
}

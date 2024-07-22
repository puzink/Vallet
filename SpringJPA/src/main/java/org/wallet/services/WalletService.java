package org.wallet.services;

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
public class WalletService {

    private final WalletRepository walletRepository;

    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }


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

    public long deposit(UUID uuid, long amount) {
        Wallet wallet = walletRepository.findById(uuid)
                .orElse(new Wallet(uuid, 0L));
        wallet.setAmount(wallet.getAmount() + amount);
        walletRepository.save(wallet);
        return wallet.getAmount();
    }
}

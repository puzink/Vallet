package org.wallet.services;

import lombok.RequiredArgsConstructor;
import org.postgresql.util.PSQLException;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.wallet.dao.WalletDao;
import org.wallet.dao.domain.tables.records.WalletRecord;
import org.wallet.model.NotEnoughFundsException;
import org.wallet.model.WalletNotFoundException;

import java.util.Optional;
import java.util.UUID;

@Service
@Profile("jdbc")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS)
public class JdbcWalletService implements WalletService{

    private final WalletDao walletDao;

    @Override
    public long withdraw(UUID uuid, long amount) {
        try {
            Optional<WalletRecord> updatedWallet = walletDao.update(uuid, amount);
            return updatedWallet.orElseThrow(() -> new WalletNotFoundException("Wallet not found: " + uuid))
                    .getAmount();
        } catch (DataIntegrityViolationException e){
            throw new NotEnoughFundsException("Not enough funds. Amount to withdraw: " + amount, e);
        }
    }

    @Override
    public long deposit(UUID uuid, long amount) {
        return walletDao.insertOnExistingUpdate(uuid, amount)
                .getAmount();
    }

}

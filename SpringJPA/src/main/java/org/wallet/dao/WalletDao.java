package org.wallet.dao;

import lombok.RequiredArgsConstructor;
import org.jooq.DSLContext;
import org.jooq.Record1;
import org.jooq.impl.DSL;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.wallet.dao.domain.tables.Wallet;
import org.wallet.dao.domain.tables.records.WalletRecord;

import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class WalletDao {

    private final DSLContext dsl;

    public WalletRecord insertOnExistingUpdate(UUID id, long amount) {
        WalletRecord res = dsl.insertInto(Wallet.WALLET)
                .set(new WalletRecord(amount, id))
                .onDuplicateKeyUpdate()
                .set(Wallet.WALLET.AMOUNT,
                        DSL.excluded(Wallet.WALLET.AMOUNT).plus(Wallet.WALLET.AMOUNT))
                .returningResult(Wallet.WALLET)
                .fetchOne().value1();

        return res;
    }


    public Optional<WalletRecord> update(UUID uuid, long amount) {

        Record1<WalletRecord> record = dsl.update(Wallet.WALLET)
                .set(Wallet.WALLET.AMOUNT, Wallet.WALLET.AMOUNT.minus(amount))
                .where(Wallet.WALLET.UUID.eq(uuid))
                .returningResult(Wallet.WALLET)
                .fetchOne();

        return Optional.ofNullable(record == null
                ? null
                : record.value1());
    }
}

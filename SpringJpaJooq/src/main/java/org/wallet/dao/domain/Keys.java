/*
 * This file is generated by jOOQ.
 */
package org.wallet.dao.domain;


import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.DSL;
import org.jooq.impl.Internal;
import org.wallet.dao.domain.tables.Wallet;
import org.wallet.dao.domain.tables.records.WalletRecord;


/**
 * A class modelling foreign key relationships and constraints of tables in
 * public.
 */
@SuppressWarnings({ "all", "unchecked", "rawtypes", "this-escape" })
public class Keys {

    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<WalletRecord> WALLET_PKEY = Internal.createUniqueKey(Wallet.WALLET, DSL.name("wallet_pkey"), new TableField[] { Wallet.WALLET.UUID }, true);
}
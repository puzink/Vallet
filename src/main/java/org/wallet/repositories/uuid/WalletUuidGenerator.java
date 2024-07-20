package org.wallet.repositories.uuid;

import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.generator.EventType;
import org.hibernate.generator.GeneratorCreationContext;
import org.hibernate.id.factory.spi.CustomIdGeneratorCreationContext;
import org.hibernate.id.uuid.UuidGenerator;
import org.wallet.entities.Wallet;

import java.lang.reflect.Member;


/**
 * Если сущности нет в БД, но у неё задан uuid, тогда используем его, вместо генерации нового.
 */
public class WalletUuidGenerator extends UuidGenerator {
    public WalletUuidGenerator(org.wallet.repositories.uuid.annotations.WalletUuidGenerator config,
                               Member idMember,
                               CustomIdGeneratorCreationContext creationContext) {
        super(config.value(), idMember, creationContext);
    }

    public WalletUuidGenerator(org.wallet.repositories.uuid.annotations.WalletUuidGenerator config,
                               Member member,
                               GeneratorCreationContext creationContext) {
        super(config.value(), member, creationContext);
    }

    @Override
    public Object generate(SharedSessionContractImplementor session,
                           Object owner,
                           Object currentValue,
                           EventType eventType) {
        if (owner instanceof Wallet wallet && wallet.getUuid() != null) {
            return wallet.getUuid();
        }
        return super.generate(session, owner, currentValue, eventType);
    }
}

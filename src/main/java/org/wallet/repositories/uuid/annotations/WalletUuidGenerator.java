package org.wallet.repositories.uuid.annotations;


import org.hibernate.annotations.IdGeneratorType;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.annotations.ValueGenerationType;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@IdGeneratorType(org.wallet.repositories.uuid.WalletUuidGenerator.class)
@ValueGenerationType(generatedBy = org.wallet.repositories.uuid.WalletUuidGenerator.class)
@Retention(RUNTIME)
@Target({FIELD, METHOD})
public @interface WalletUuidGenerator {

    UuidGenerator value() default @UuidGenerator;

}

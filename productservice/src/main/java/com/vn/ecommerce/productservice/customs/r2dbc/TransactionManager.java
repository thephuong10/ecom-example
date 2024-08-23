package com.vn.ecommerce.productservice.customs.r2dbc;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.r2dbc.connection.R2dbcTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.Optional;

public class TransactionManager extends R2dbcTransactionManager {

    private static final Integer MAX_TRANSACTION_NAME_LENGTH = 32;

    public TransactionManager(ConnectionFactory connectionFactory) {
        super(connectionFactory);
    }

    @Override
    protected io.r2dbc.spi.TransactionDefinition createTransactionDefinition(TransactionDefinition definition) {
        return super.createTransactionDefinition(adjustTransactionDefinintionNameLength(definition));
    }

    private DefaultTransactionDefinition adjustTransactionDefinintionNameLength(TransactionDefinition definition) {

        DefaultTransactionDefinition adjustedDefinition = new DefaultTransactionDefinition(definition);
        adjustedDefinition.setName(Optional.ofNullable(adjustedDefinition.getName())
                .map(this::shortenTransactionDefinitionName)
                .orElse(null));

        return adjustedDefinition;
    }

    private String shortenTransactionDefinitionName(String name) {

        int length = name.length();

        return length > MAX_TRANSACTION_NAME_LENGTH ? name.substring(length - MAX_TRANSACTION_NAME_LENGTH, length)
                : name;
    }

}

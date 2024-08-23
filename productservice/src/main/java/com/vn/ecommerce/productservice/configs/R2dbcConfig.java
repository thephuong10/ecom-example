package com.vn.ecommerce.productservice.configs;

import com.vn.ecommerce.productservice.customs.r2dbc.LoggingListener;
import com.vn.ecommerce.productservice.customs.r2dbc.TransactionManager;
import io.r2dbc.proxy.ProxyConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactoryOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate;
import org.springframework.transaction.ReactiveTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.reactive.TransactionalOperator;

@Configuration
@EnableTransactionManagement
public class R2dbcConfig {

    @Value("${spring.r2dbc.url}")
    private String url;

    @Value("${spring.r2dbc.username}")
    private String username;

    @Value("${spring.r2dbc.password}")
    private String password;

    @Bean
    @Primary
    public ConnectionFactory connectionFactory() {
        ConnectionFactory originalConnectionFactory = ConnectionFactories.get(
                ConnectionFactoryOptions.parse(url)
                        .mutate()
                        .option(ConnectionFactoryOptions.USER, username)
                        .option(ConnectionFactoryOptions.PASSWORD, password)
                        .build());

        return ProxyConnectionFactory.builder(originalConnectionFactory)
                .listener(new LoggingListener())
                .build();
    }

    @Bean
    public R2dbcEntityTemplate r2dbcEntityTemplate(ConnectionFactory connectionFactory) {
        return new R2dbcEntityTemplate(connectionFactory);
    }


    @Bean
    public TransactionalOperator transactionalOperator(ReactiveTransactionManager transactionManager) {
        return TransactionalOperator.create(transactionManager);
    }


    @Bean
    public ReactiveTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new TransactionManager(connectionFactory);
    }


}

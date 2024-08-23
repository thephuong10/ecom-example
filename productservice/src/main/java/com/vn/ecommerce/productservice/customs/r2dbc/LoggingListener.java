package com.vn.ecommerce.productservice.customs.r2dbc;

import io.r2dbc.proxy.listener.ProxyExecutionListener;
import io.r2dbc.proxy.core.QueryInfo;
import io.r2dbc.proxy.core.QueryExecutionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class LoggingListener implements ProxyExecutionListener {

    private final Logger logger = LoggerFactory.getLogger(LoggingListener.class);

    @Override
    public void beforeQuery(QueryExecutionInfo execInfo) {
        for (QueryInfo queryInfo : execInfo.getQueries()) {
            logger.debug("Executing query: {}", queryInfo.getQuery());
            System.out.println(queryInfo.getQuery());
        }
    }
}

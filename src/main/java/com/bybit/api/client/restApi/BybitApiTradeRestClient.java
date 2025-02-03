package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;

import java.io.IOException;
import java.util.Map;

public interface BybitApiTradeRestClient {

    // Trade
    GenericResponse<?> getOrderHistory(TradeOrderRequest orderHistoryRequest);
    GenericResponse<?> setDisconnectCancelAllTime(TradeOrderRequest setDcpRequest);
    GenericResponse<?> getBorrowQuota(TradeOrderRequest borrowQuotaRequest);
    GenericResponse<?> getOpenOrders(TradeOrderRequest order);
    GenericResponse<?> getTradeHistory(TradeOrderRequest order);
    GenericResponse<?> createOrder(TradeOrderRequest order);
    GenericResponse<?> createOrder(Map<String, GenericResponse<?>> payload);
    GenericResponse<?> createOrder(String json) throws IOException;
    GenericResponse<?> createBatchOrder(BatchOrderRequest batchOrderRequest);
    GenericResponse<?> createBathOrder(Map<String, GenericResponse<?>> payload);
    GenericResponse<?> createBathOrder(String json) throws IOException;
    GenericResponse<?> amendBatchOrder(BatchOrderRequest batchOrderRequest);
    GenericResponse<?> amendBatchOrder(Map<String, GenericResponse<?>> payload);
    GenericResponse<?> amendBatchOrder(String json) throws IOException;
    GenericResponse<?> cancelBatchOrder(BatchOrderRequest batchOrderRequest);
    GenericResponse<?> cancelBatchOrder(Map<String, GenericResponse<?>> payload);
    GenericResponse<?> cancelBatchOrder(String json) throws IOException;
    GenericResponse<?> cancelOrder(TradeOrderRequest order);
    GenericResponse<?> cancelAllOrder(TradeOrderRequest order);
    GenericResponse<?> amendOrder(TradeOrderRequest order);
}

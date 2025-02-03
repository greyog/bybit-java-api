package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;

import java.io.IOException;
import java.util.Map;

public interface BybitApiAsyncTradeRestClient {
    // Trade
    void getOrderHistory(TradeOrderRequest orderHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setDisconnectCancelAllTime(TradeOrderRequest tradeOrderRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getBorrowQuota(TradeOrderRequest borrowQuotaRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getOpenOrders(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
    void getTradeHistory(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
    void createOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
    void createOrder(Map<String, GenericResponse<?>> order, BybitApiCallback<GenericResponse<?>> callback);
    void createOrder(String order, BybitApiCallback<GenericResponse<?>> callback) throws IOException;
    void createBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<GenericResponse<?>> callback);
    void createBathOrder(Map<String, GenericResponse<?>> payload, BybitApiCallback<GenericResponse<?>> callback);
    void createBathOrder(String json, BybitApiCallback<GenericResponse<?>> callback) throws IOException;
    void amendBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<GenericResponse<?>> callback);
    void amendBatchOrder(Map<String, GenericResponse<?>> payload, BybitApiCallback<GenericResponse<?>> callback);
    void amendBatchOrder(String json, BybitApiCallback<GenericResponse<?>> callback) throws IOException;
    void cancelBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<GenericResponse<?>> callback);
    void cancelBatchOrder(Map<String, GenericResponse<?>> payload, BybitApiCallback<GenericResponse<?>> callback);
    void cancelBatchOrder(String json, BybitApiCallback<GenericResponse<?>> callback) throws IOException;
    void cancelOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
    void cancelAllOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
    void amendOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<?>> callback);
}

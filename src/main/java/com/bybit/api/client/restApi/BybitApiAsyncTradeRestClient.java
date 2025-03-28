package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderResult;

import java.io.IOException;
import java.util.Map;

public interface BybitApiAsyncTradeRestClient {
    // Trade
    void getOrderHistory(TradeOrderRequest orderHistoryRequest, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void setDisconnectCancelAllTime(TradeOrderRequest tradeOrderRequest, BybitApiCallback<Object> callback);
    void getBorrowQuota(TradeOrderRequest borrowQuotaRequest, BybitApiCallback<Object> callback);
    void getOpenOrders(TradeOrderRequest order, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void getTradeHistory(TradeOrderRequest order, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void createOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void createOrder(Map<String, Object> order, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void createOrder(String order, BybitApiCallback<GenericResponse<OrderResult>> callback) throws IOException;
    void createBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void createBathOrder(Map<String, Object> payload, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void createBathOrder(String json, BybitApiCallback<GenericResponse<OrderResult>> callback) throws IOException;
    void amendBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<Object> callback);
    void amendBatchOrder(Map<String, Object> payload, BybitApiCallback<Object> callback);
    void amendBatchOrder(String json, BybitApiCallback<Object> callback) throws IOException;
    void cancelBatchOrder(BatchOrderRequest batchOrderRequest, BybitApiCallback<Object> callback);
    void cancelBatchOrder(Map<String, Object> payload, BybitApiCallback<Object> callback);
    void cancelBatchOrder(String json, BybitApiCallback<Object> callback) throws IOException;
    void cancelOrder(TradeOrderRequest order, BybitApiCallback<Object> callback);
    void cancelAllOrder(TradeOrderRequest order, BybitApiCallback<GenericResponse<OrderResult>> callback);
    void amendOrder(TradeOrderRequest order, BybitApiCallback<Object> callback);
}

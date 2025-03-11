package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.position.request.PositionDataRequest;
import com.bybit.api.client.domain.trade.request.BatchOrderRequest;
import com.bybit.api.client.domain.trade.request.TradeOrderRequest;
import com.bybit.api.client.domain.trade.response.OrderResult;

import java.io.IOException;
import java.util.Map;

public interface BybitApiTradeRestClient {

    // Trade
    GenericResponse<OrderResult> getOrderHistory(TradeOrderRequest orderHistoryRequest);
    Object setDisconnectCancelAllTime(TradeOrderRequest setDcpRequest);
    Object getBorrowQuota(TradeOrderRequest borrowQuotaRequest);
    GenericResponse<OrderResult> getOpenOrders(TradeOrderRequest order);
    Object getTradeHistory(TradeOrderRequest order);
    GenericResponse<OrderResult> createOrder(TradeOrderRequest order);
    Object createOrder(Map<String, Object> payload);
    Object createOrder(String json) throws IOException;
    GenericResponse<OrderResult> createBatchOrder(BatchOrderRequest batchOrderRequest);
    Object createBathOrder(Map<String, Object> payload);
    Object createBathOrder(String json) throws IOException;
    Object amendBatchOrder(BatchOrderRequest batchOrderRequest);
    Object amendBatchOrder(Map<String, Object> payload);
    Object amendBatchOrder(String json) throws IOException;
    Object cancelBatchOrder(BatchOrderRequest batchOrderRequest);
    Object cancelBatchOrder(Map<String, Object> payload);
    Object cancelBatchOrder(String json) throws IOException;
    Object cancelOrder(TradeOrderRequest order);
    GenericResponse<OrderResult> cancelAllOrder(TradeOrderRequest order);
    Object amendOrder(TradeOrderRequest order);
}

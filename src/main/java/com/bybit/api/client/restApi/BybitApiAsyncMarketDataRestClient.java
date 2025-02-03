package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.announcement.request.AnnouncementInfoRequest;
import com.bybit.api.client.domain.market.request.MarketDataRequest;

public interface BybitApiAsyncMarketDataRestClient {
    // Market endpoints
    void getServerTime(BybitApiCallback<GenericResponse<?>> callback);
    void getMarketLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getMarketPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getIndexPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getPremiumIndexPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInstrumentsInfo(MarketDataRequest instrumentInfoRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getMarketOrderBook(MarketDataRequest marketOrderBookRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getMarketTickers(MarketDataRequest marketDataTickerRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getFundingHistory(MarketDataRequest fundingHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getRecentTradeData(MarketDataRequest recentTradeRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getOpenInterest(MarketDataRequest openInterestRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getHistoricalVolatility(MarketDataRequest historicalVolatilityRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsurance(MarketDataRequest marketDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getInsurance(BybitApiCallback<GenericResponse<?>> callback);
    void getRiskLimit(MarketDataRequest marketRiskLimitRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getDeliveryPrice(MarketDataRequest deliveryPriceRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getMarketAccountRatio(MarketDataRequest marketAccountRatioRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAnnouncementInfo(MarketDataRequest announcementInfoRequest, BybitApiCallback<GenericResponse<?>> callback);
}

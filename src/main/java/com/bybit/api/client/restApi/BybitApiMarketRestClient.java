package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.announcement.request.AnnouncementInfoRequest;
import com.bybit.api.client.domain.market.request.MarketDataRequest;

public interface BybitApiMarketRestClient {
    // Market Data
    GenericResponse<?> getServerTime();
    GenericResponse<?> getMarketLinesData(MarketDataRequest marketKlineRequest);
    GenericResponse<?> getMarketPriceLinesData(MarketDataRequest marketKlineRequest);
    GenericResponse<?> getIndexPriceLinesData(MarketDataRequest marketKlineRequest);
    GenericResponse<?> getPremiumIndexPriceLinesData(MarketDataRequest marketKlineRequest);
    GenericResponse<?> getInstrumentsInfo(MarketDataRequest instrumentInfoRequest);
    GenericResponse<?> getMarketOrderBook(MarketDataRequest marketOrderBookRequest);
    GenericResponse<?> getMarketTickers(MarketDataRequest marketDataTickerRequest);
    GenericResponse<?> getFundingHistory(MarketDataRequest fundingHistoryRequest);
    GenericResponse<?> getRecentTradeData(MarketDataRequest recentTradeRequest);
    GenericResponse<?> getOpenInterest(MarketDataRequest openInterestRequest);
    GenericResponse<?> getHistoricalVolatility(MarketDataRequest HistoricalVolatilityRequest);
    GenericResponse<?> getInsurance(MarketDataRequest marketDataRequest);
    GenericResponse<?> getInsurance();
    GenericResponse<?> getRiskLimit(MarketDataRequest marketRiskLimitRequest);
    GenericResponse<?> getDeliveryPrice(MarketDataRequest deliveryPriceRequest);
    GenericResponse<?> getMarketAccountRatio(MarketDataRequest marketAccountRatioRequest);
    GenericResponse<?> getAnnouncementInfo(MarketDataRequest announcementInfoRequest);
}

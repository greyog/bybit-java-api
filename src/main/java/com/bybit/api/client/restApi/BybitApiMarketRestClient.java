package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.announcement.request.AnnouncementInfoRequest;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import com.bybit.api.client.domain.market.response.instrumentInfo.InstrumentInfoResult;
import com.bybit.api.client.domain.market.response.serverTime.ServerTimeResult;
import com.bybit.api.client.domain.market.response.tickers.TickersResult;

public interface BybitApiMarketRestClient {
    // Market Data
    GenericResponse<ServerTimeResult> getServerTime();
    Object getMarketLinesData(MarketDataRequest marketKlineRequest);
    Object getMarketPriceLinesData(MarketDataRequest marketKlineRequest);
    Object getIndexPriceLinesData(MarketDataRequest marketKlineRequest);
    Object getPremiumIndexPriceLinesData(MarketDataRequest marketKlineRequest);
    GenericResponse<InstrumentInfoResult> getInstrumentsInfo(MarketDataRequest instrumentInfoRequest);
    Object getMarketOrderBook(MarketDataRequest marketOrderBookRequest);
    GenericResponse<TickersResult> getMarketTickers(MarketDataRequest marketDataTickerRequest);
    Object getFundingHistory(MarketDataRequest fundingHistoryRequest);
    Object getRecentTradeData(MarketDataRequest recentTradeRequest);
    Object getOpenInterest(MarketDataRequest openInterestRequest);
    Object getHistoricalVolatility(MarketDataRequest HistoricalVolatilityRequest);
    Object getInsurance(MarketDataRequest marketDataRequest);
    Object getInsurance();
    Object getRiskLimit(MarketDataRequest marketRiskLimitRequest);
    Object getDeliveryPrice(MarketDataRequest deliveryPriceRequest);
    Object getMarketAccountRatio(MarketDataRequest marketAccountRatioRequest);
    Object getAnnouncementInfo(MarketDataRequest announcementInfoRequest);
}

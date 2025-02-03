package com.bybit.api.client.impl;

import com.bybit.api.client.restApi.BybitApiAsyncMarketDataRestClient;
import com.bybit.api.client.restApi.BybitApiCallback;
import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.domain.market.request.MarketDataRequest;

import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;

public class BybitApiMarketAsyncRestClientImpl implements BybitApiAsyncMarketDataRestClient {
    private final BybitApiService bybitApiService;

    public BybitApiMarketAsyncRestClientImpl(String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, baseUrl, debugMode, recvWindow, logOption);
    }
    // Market Data endpoints
    @Override
    public void getServerTime(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getServerTime().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getMarketLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getMarketLinesData(
                marketKlineRequest.getCategory().getCategoryTypeId(),
                marketKlineRequest.getSymbol(),
                marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                marketKlineRequest.getStart(),
                marketKlineRequest.getEnd(),
                marketKlineRequest.getLimit()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getMarketPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getMarketPriceLinesData(
                marketKlineRequest.getCategory().getCategoryTypeId(),
                marketKlineRequest.getSymbol(),
                marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                marketKlineRequest.getStart(),
                marketKlineRequest.getEnd(),
                marketKlineRequest.getLimit()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getIndexPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getIndexPriceLinesData(
                marketKlineRequest.getCategory().getCategoryTypeId(),
                marketKlineRequest.getSymbol(),
                marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                marketKlineRequest.getStart(),
                marketKlineRequest.getEnd(),
                marketKlineRequest.getLimit()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getPremiumIndexPriceLinesData(MarketDataRequest marketKlineRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getPremiumIndexPriceLinesData(
                marketKlineRequest.getCategory().getCategoryTypeId(),
                marketKlineRequest.getSymbol(),
                marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                marketKlineRequest.getStart(),
                marketKlineRequest.getEnd(),
                marketKlineRequest.getLimit()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getInstrumentsInfo(MarketDataRequest instrumentInfoRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getInstrumentsInfo(
                instrumentInfoRequest.getCategory().getCategoryTypeId(),
                instrumentInfoRequest.getSymbol(),
                instrumentInfoRequest.getInstrumentStatus() == null ? null : instrumentInfoRequest.getInstrumentStatus().getStatus(),
                instrumentInfoRequest.getBaseCoin(),
                instrumentInfoRequest.getLimit(),
                instrumentInfoRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getMarketOrderBook(MarketDataRequest marketOrderBookRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getMarketOrderBook(
                marketOrderBookRequest.getCategory().getCategoryTypeId(),
                marketOrderBookRequest.getSymbol(),
                marketOrderBookRequest.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getMarketTickers(MarketDataRequest marketDataTickerRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getMarketTickers(
                marketDataTickerRequest.getCategory().getCategoryTypeId(),
                marketDataTickerRequest.getSymbol(),
                marketDataTickerRequest.getBaseCoin(),
                marketDataTickerRequest.getExpDate()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getFundingHistory(MarketDataRequest fundingHistoryRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getFundingHistory(
                fundingHistoryRequest.getCategory().getCategoryTypeId(),
                fundingHistoryRequest.getSymbol(),
                fundingHistoryRequest.getStartTime(),
                fundingHistoryRequest.getEndTime(),
                fundingHistoryRequest.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getRecentTradeData(MarketDataRequest recentTradeRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getRecentTradeData(
                recentTradeRequest.getCategory().getCategoryTypeId(),
                recentTradeRequest.getSymbol(),
                recentTradeRequest.getBaseCoin(),
                recentTradeRequest.getOptionType() == null ? null : recentTradeRequest.getOptionType().getOpType(),
                recentTradeRequest.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getOpenInterest(MarketDataRequest openInterestRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getOpenInterest(
                openInterestRequest.getCategory().getCategoryTypeId(),
                openInterestRequest.getSymbol(),
                openInterestRequest.getMarketInterval() == null ? null : openInterestRequest.getMarketInterval().getIntervalId(),
                openInterestRequest.getStartTime(),
                openInterestRequest.getEndTime(),
                openInterestRequest.getLimit(),
                openInterestRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getHistoricalVolatility(MarketDataRequest historicalVolatilityRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getHistoricalVolatility(
                historicalVolatilityRequest.getCategory().getCategoryTypeId(),
                historicalVolatilityRequest.getBaseCoin(),
                historicalVolatilityRequest.getOptionPeriod(),
                historicalVolatilityRequest.getStartTime(),
                historicalVolatilityRequest.getEndTime()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getInsurance(MarketDataRequest marketDataRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getInsurance(marketDataRequest.getCoin()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getInsurance(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getInsurance().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getRiskLimit(MarketDataRequest marketRiskLimitRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getRiskLimit(
                        marketRiskLimitRequest.getCategory().getCategoryTypeId(),
                        marketRiskLimitRequest.getSymbol()
                ).
                enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getDeliveryPrice(MarketDataRequest deliveryPriceRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getDeliveryPrice(deliveryPriceRequest.getCategory().getCategoryTypeId(),
                deliveryPriceRequest.getSymbol(),
                deliveryPriceRequest.getBaseCoin(),
                deliveryPriceRequest.getLimit(),
                deliveryPriceRequest.getCursor()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getMarketAccountRatio(MarketDataRequest marketAccountRatioRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getMarketAccountRatio(marketAccountRatioRequest.getCategory().getCategoryTypeId(),
                marketAccountRatioRequest.getSymbol(),
                marketAccountRatioRequest.getDataRecordingPeriod() == null ? null : marketAccountRatioRequest.getDataRecordingPeriod().getPeriod(),
                marketAccountRatioRequest.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAnnouncementInfo(MarketDataRequest announcementInfoRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAnouncementInfo(
                announcementInfoRequest.getLocale() == null ? null : announcementInfoRequest.getLocale().getLanguageSymbol(),
                announcementInfoRequest.getType() == null ? null : announcementInfoRequest.getType().getAnnouncementType(),
                announcementInfoRequest.getTag() == null ? null : announcementInfoRequest.getTag().getEnglishTranslation(),
                announcementInfoRequest.getPage(),
                announcementInfoRequest.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }
}

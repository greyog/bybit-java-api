package com.bybit.api.client.impl;

import com.bybit.api.client.restApi.BybitApiMarketRestClient;
import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.domain.market.request.MarketDataRequest;
import lombok.Getter;

import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;
import static com.bybit.api.client.service.BybitApiServiceGenerator.executeSync;

@Getter
public class BybitApiMarketRestClientImpl implements BybitApiMarketRestClient {
    private final BybitApiService bybitApiService;

    public BybitApiMarketRestClientImpl(String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, baseUrl, debugMode, recvWindow, logOption);
    }

    // Market Data endpoints
    @Override
    public GenericResponse<?> getAnnouncementInfo(MarketDataRequest announcementInfoRequest) {
        return executeSync(bybitApiService.getAnouncementInfo(
                announcementInfoRequest.getLocale() == null ? null : announcementInfoRequest.getLocale().getLanguageSymbol(),
                announcementInfoRequest.getType() == null ? null : announcementInfoRequest.getType().getAnnouncementType(),
                announcementInfoRequest.getTag() == null ? null : announcementInfoRequest.getTag().getEnglishTranslation(),
                announcementInfoRequest.getPage(),
                announcementInfoRequest.getLimit()
        ));
    }

    @Override
    public GenericResponse<?> getServerTime() {
        return executeSync(
                bybitApiService.getServerTime());
    }

    @Override
    public GenericResponse<?> getMarketLinesData(MarketDataRequest marketKlineRequest) {
        return executeSync(
                bybitApiService.getMarketLinesData(
                        marketKlineRequest.getCategory().getCategoryTypeId(),
                        marketKlineRequest.getSymbol(),
                        marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                        marketKlineRequest.getStart(),
                        marketKlineRequest.getEnd(),
                        marketKlineRequest.getLimit()
                )
        );
    }

    @Override
    public GenericResponse<?> getMarketPriceLinesData(MarketDataRequest marketKlineRequest) {
        return executeSync(
                bybitApiService.getMarketPriceLinesData(
                        marketKlineRequest.getCategory().getCategoryTypeId(),
                        marketKlineRequest.getSymbol(),
                        marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                        marketKlineRequest.getStart(),
                        marketKlineRequest.getEnd(),
                        marketKlineRequest.getLimit()
                )
        );
    }

    @Override
    public GenericResponse<?> getIndexPriceLinesData(MarketDataRequest marketKlineRequest) {
        return executeSync(
                bybitApiService.getIndexPriceLinesData(
                        marketKlineRequest.getCategory().getCategoryTypeId(),
                        marketKlineRequest.getSymbol(),
                        marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                        marketKlineRequest.getStart(),
                        marketKlineRequest.getEnd(),
                        marketKlineRequest.getLimit()
                )
        );
    }

    @Override
    public GenericResponse<?> getPremiumIndexPriceLinesData(MarketDataRequest marketKlineRequest) {
        return executeSync(
                bybitApiService.getPremiumIndexPriceLinesData(
                        marketKlineRequest.getCategory().getCategoryTypeId(),
                        marketKlineRequest.getSymbol(),
                        marketKlineRequest.getMarketInterval() == null ? null : marketKlineRequest.getMarketInterval().getIntervalId(),
                        marketKlineRequest.getStart(),
                        marketKlineRequest.getEnd(),
                        marketKlineRequest.getLimit()
                )
        );
    }

    @Override
    public GenericResponse<?> getInstrumentsInfo(MarketDataRequest instrumentInfoRequest) {
        return executeSync(bybitApiService.getInstrumentsInfo(
                instrumentInfoRequest.getCategory().getCategoryTypeId(),
                instrumentInfoRequest.getSymbol(),
                instrumentInfoRequest.getInstrumentStatus() == null ? null : instrumentInfoRequest.getInstrumentStatus().getStatus(),
                instrumentInfoRequest.getBaseCoin(),
                instrumentInfoRequest.getLimit(),
                instrumentInfoRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getMarketOrderBook(MarketDataRequest marketOrderBookRequest) {
        return executeSync(bybitApiService.getMarketOrderBook(
                marketOrderBookRequest.getCategory().getCategoryTypeId(),
                marketOrderBookRequest.getSymbol(),
                marketOrderBookRequest.getLimit()
        ));
    }

    @Override
    public GenericResponse<?> getMarketTickers(MarketDataRequest marketDataTickerRequest) {
        return executeSync(bybitApiService.getMarketTickers(
                marketDataTickerRequest.getCategory().getCategoryTypeId(),
                marketDataTickerRequest.getSymbol(),
                marketDataTickerRequest.getBaseCoin(),
                marketDataTickerRequest.getExpDate()
        ));
    }

    @Override
    public GenericResponse<?> getFundingHistory(MarketDataRequest fundingHistoryRequest) {
        return executeSync(bybitApiService.getFundingHistory(
                fundingHistoryRequest.getCategory().getCategoryTypeId(),
                fundingHistoryRequest.getSymbol(),
                fundingHistoryRequest.getStartTime(),
                fundingHistoryRequest.getEndTime(),
                fundingHistoryRequest.getLimit()
        ));
    }

    @Override
    public GenericResponse<?> getRecentTradeData(MarketDataRequest recentTradeRequest) {
        return executeSync(bybitApiService.getRecentTradeData(
                recentTradeRequest.getCategory().getCategoryTypeId(),
                recentTradeRequest.getSymbol(),
                recentTradeRequest.getBaseCoin(),
                recentTradeRequest.getOptionType() == null ? null : recentTradeRequest.getOptionType().getOpType(),
                recentTradeRequest.getLimit()
        ));
    }

    @Override
    public GenericResponse<?> getOpenInterest(MarketDataRequest openInterestRequest) {
        return executeSync(bybitApiService.getOpenInterest(
                openInterestRequest.getCategory().getCategoryTypeId(),
                openInterestRequest.getSymbol(),
                openInterestRequest.getMarketIntervalTime() == null ? null : openInterestRequest.getMarketIntervalTime().getInterval(),
                openInterestRequest.getStartTime(),
                openInterestRequest.getEndTime(),
                openInterestRequest.getLimit(),
                openInterestRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getHistoricalVolatility(MarketDataRequest historicalVolatilityRequest) {
        return executeSync(bybitApiService.getHistoricalVolatility(
                historicalVolatilityRequest.getCategory().getCategoryTypeId(),
                historicalVolatilityRequest.getBaseCoin(),
                historicalVolatilityRequest.getOptionPeriod(),
                historicalVolatilityRequest.getStartTime(),
                historicalVolatilityRequest.getEndTime())
        );
    }

    @Override
    public GenericResponse<?> getRiskLimit(MarketDataRequest marketRiskLimitRequest) {
        return executeSync(bybitApiService.getRiskLimit(
                marketRiskLimitRequest.getCategory().getCategoryTypeId(),
                marketRiskLimitRequest.getSymbol()));
    }

    @Override
    public GenericResponse<?> getInsurance(MarketDataRequest marketDataRequest) {
        return executeSync(bybitApiService.getInsurance(marketDataRequest.getCoin()));
    }

    @Override
    public GenericResponse<?> getInsurance() {
        return executeSync(bybitApiService.getInsurance());
    }

    @Override
    public GenericResponse<?> getDeliveryPrice(MarketDataRequest deliveryPriceRequest) {
        return executeSync(bybitApiService.getDeliveryPrice(deliveryPriceRequest.getCategory().getCategoryTypeId(),
                deliveryPriceRequest.getSymbol(),
                deliveryPriceRequest.getBaseCoin(),
                deliveryPriceRequest.getLimit(),
                deliveryPriceRequest.getCursor()
        ));
    }

    @Override
    public GenericResponse<?> getMarketAccountRatio(MarketDataRequest marketAccountRatioRequest) {
        return executeSync(bybitApiService.getMarketAccountRatio(marketAccountRatioRequest.getCategory().getCategoryTypeId(),
                marketAccountRatioRequest.getSymbol(),
                marketAccountRatioRequest.getDataRecordingPeriod() == null ? null : marketAccountRatioRequest.getDataRecordingPeriod().getPeriod(),
                marketAccountRatioRequest.getLimit()
        ));
    }
}

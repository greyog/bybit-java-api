package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.spot.SpotMarginDataRequest;

public interface BybitApiAsyncSpotMarginRestClient {
    // Spot Endpoints
    // Spot Leverage Token
    void getSpotLeverageTokenInfo(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getSpotLeverageTokenMarket(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void purchaseSpotLeverageToken(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void redeemSpotLeverageToken(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getSpotLeverageRecords(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    // Spot Margin UTA
    void getUtaVipSpotMarginTradeData(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void setUTASpotMarginTrade(String mode, BybitApiCallback<GenericResponse<?>> callback);

    void setUTASpotMarginTradeLeverage(String leverage, BybitApiCallback<GenericResponse<?>> callback);

    void getUTASpotMarginTradeLeverageState(BybitApiCallback<GenericResponse<?>> callback);

    // Spot Margin Normal
    void getNormalVipSpotMarginTradeData(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeCoinInfo(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeBorrowCoinInfo(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeInterestQuota(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeAccountInfo(BybitApiCallback<GenericResponse<?>> callback);

    void setNormalSpotToggleMarginTrade(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void loanNormalSpotMarginTrade(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void repayNormalSpotMarginTrade(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeBorrowOrders(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getNormalSpotMarginTradeRepayOrders(SpotMarginDataRequest spotMarginDataRequest, BybitApiCallback<GenericResponse<?>> callback);
}

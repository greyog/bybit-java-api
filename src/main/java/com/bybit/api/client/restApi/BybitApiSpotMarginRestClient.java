package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.spot.SpotMarginDataRequest;

public interface BybitApiSpotMarginRestClient {
    // Spot Endpoints
    // Spot Leverage Token
    GenericResponse<?> getSpotLeverageTokenInfo(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getSpotLeverageTokenMarket(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> purchaseSpotLeverageToken(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> redeemSpotLeverageToken(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getSpotLeverageRecords(SpotMarginDataRequest spotMarginDataRequest);

    // Spot Margin UTA
    GenericResponse<?> getUtaVipSpotMarginTradeData(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> setUTASpotMarginTrade(String mode);
    GenericResponse<?> setUTASpotMarginTradeLeverage(String leverage);
    GenericResponse<?> getUTASpotMarginTradeLeverageState();

    // Spot Margin Normal
    GenericResponse<?> getNormalVipSpotMarginTradeData(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeCoinInfo(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeBorrowCoinInfo(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeInterestQuota(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeAccountInfo();
    GenericResponse<?> setNormalSpotToggleMarginTrade(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> loanNormalSpotMarginTrade(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> repayNormalSpotMarginTrade(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeBorrowOrders(SpotMarginDataRequest spotMarginDataRequest);
    GenericResponse<?> getNormalSpotMarginTradeRepayOrders(SpotMarginDataRequest spotMarginDataRequest);
}

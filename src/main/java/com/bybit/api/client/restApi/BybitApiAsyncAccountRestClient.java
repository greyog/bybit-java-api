package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.account.request.BatchSetCollateralCoinRequest;

public interface BybitApiAsyncAccountRestClient {
    // Account endpoints
    void getWalletBalance(AccountDataRequest walletBalanceRequest, BybitApiCallback<GenericResponse<?>> callback);
    void upgradeAccountToUTA(BybitApiCallback<GenericResponse<?>> callback);
    void getAccountBorrowHistory(AccountDataRequest borrowHistoryRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setAccountCollateralCoin(AccountDataRequest setCollateralCoinRequest, BybitApiCallback<GenericResponse<?>> callback);
    void batchSetAccountCollateralCoin(BatchSetCollateralCoinRequest batchSetCollateralCoinRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountCollateralInfo(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountCoinGeeks(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountFreeRate(AccountDataRequest getFeeRateRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountInfo(BybitApiCallback<GenericResponse<?>> callback);
    void getTransactionLog(AccountDataRequest getTransactionLogRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setAccountMarginMode(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void modifyAccountMMP(AccountDataRequest setMMPRequest, BybitApiCallback<GenericResponse<?>> callback);
    void resetAccountMMP(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountMMPState(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAccountSMPGroup(BybitApiCallback<GenericResponse<?>> callback);
    void setAccountSpotHedging(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
}

package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.GenericResponse;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.domain.account.request.BatchSetCollateralCoinRequest;

public interface BybitApiAccountRestClient {
    // Account endpoints
    GenericResponse<?> getWalletBalance(AccountDataRequest walletBalanceRequest);
    GenericResponse<?> upgradeAccountToUTA();
    GenericResponse<?> getAccountBorrowHistory(AccountDataRequest borrowHistoryRequest);
    GenericResponse<?> setAccountCollateralCoin(AccountDataRequest setCollateralCoinRequest);
    GenericResponse<?> batchSetAccountCollateralCoin(BatchSetCollateralCoinRequest batchSetCollateralCoinRequest);
    GenericResponse<?> getAccountCollateralInfo(AccountDataRequest request);
    GenericResponse<?> getAccountCoinGeeks(AccountDataRequest request);
    GenericResponse<?> getAccountFreeRate(AccountDataRequest getFeeRateRequest);
    GenericResponse<?> getAccountInfo();
    GenericResponse<?> getTransactionLog(AccountDataRequest getTransactionLogRequest);
    GenericResponse<?> setAccountMarginMode(AccountDataRequest request);
    GenericResponse<?> setAccountSpotHedging(AccountDataRequest request);
    GenericResponse<?> modifyAccountMMP(AccountDataRequest setMMPRequest);
    GenericResponse<?> resetAccountMMP(AccountDataRequest request);
    GenericResponse<?> getAccountMMPState(AccountDataRequest request);
    GenericResponse<?> getAccountSMPGroup();
}

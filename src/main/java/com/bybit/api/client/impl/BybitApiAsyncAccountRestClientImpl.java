package com.bybit.api.client.impl;

import com.bybit.api.client.domain.account.request.BatchSetCollateralCoinRequest;
import com.bybit.api.client.restApi.BybitApiAsyncAccountRestClient;
import com.bybit.api.client.restApi.BybitApiCallback;
import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.domain.account.request.AccountDataRequest;
import com.bybit.api.client.service.BybitJsonConverter;

import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;

public class BybitApiAsyncAccountRestClientImpl implements BybitApiAsyncAccountRestClient {
    private final BybitApiService bybitApiService;
    private final BybitJsonConverter converter = new BybitJsonConverter();

    public BybitApiAsyncAccountRestClientImpl(String apiKey, String secret, String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, apiKey, secret, baseUrl, debugMode, recvWindow, logOption, "");
    }

    // Account Endpoints
    @Override
    public void getWalletBalance(AccountDataRequest walletBalanceRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getWalletBalance(
                walletBalanceRequest.getAccountType().getAccountTypeValue(),
                walletBalanceRequest.getCoins()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void upgradeAccountToUTA(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.upgradeAccountToUTA().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountBorrowHistory(AccountDataRequest borrowHistoryRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountBorrowHistory(
                borrowHistoryRequest.getCurrency(),
                borrowHistoryRequest.getStartTime(),
                borrowHistoryRequest.getEndTime(),
                borrowHistoryRequest.getLimit(),
                borrowHistoryRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void setAccountCollateralCoin(AccountDataRequest setCollateralCoinRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToSetCollateralCoinRequest(setCollateralCoinRequest);
        bybitApiService.setAccountCollateralCoin(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void batchSetAccountCollateralCoin(BatchSetCollateralCoinRequest batchSetCollateralCoinRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.batchSetAccountCollateralCoin(batchSetCollateralCoinRequest).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountCollateralInfo(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountCollateralInfo(
                request.getCurrency()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountCoinGeeks(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountCoinGeeks(request.getBaseCoin()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountFreeRate(AccountDataRequest getFeeRateRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountFreeRate(
                getFeeRateRequest.getCategory().getCategoryTypeId(),
                getFeeRateRequest.getSymbol(),
                getFeeRateRequest.getBaseCoin()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountInfo(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountInfo().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getTransactionLog(AccountDataRequest getTransactionLogRequest, BybitApiCallback<GenericResponse<?>> callback) {
        boolean isUta = getTransactionLogRequest.getIsUta() == null || getTransactionLogRequest.getIsUta().isUta();
        if(isUta)
        bybitApiService.getUtaTransactionLog(
                getTransactionLogRequest.getAccountType() == null ? null : getTransactionLogRequest.getAccountType().getAccountTypeValue(),
                getTransactionLogRequest.getCategory() == null ? null : getTransactionLogRequest.getCategory().getCategoryTypeId(),
                getTransactionLogRequest.getCurrency(),
                getTransactionLogRequest.getBaseCoin(),
                getTransactionLogRequest.getTransactionType() == null ? null : getTransactionLogRequest.getTransactionType().getTransactionTypeId(),
                getTransactionLogRequest.getStartTime(),
                getTransactionLogRequest.getEndTime(),
                getTransactionLogRequest.getLimit(),
                getTransactionLogRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
        else
            bybitApiService.getClassicalTransactionLog(
                    getTransactionLogRequest.getAccountType() == null ? null : getTransactionLogRequest.getAccountType().getAccountTypeValue(),
                    getTransactionLogRequest.getCategory() == null ? null : getTransactionLogRequest.getCategory().getCategoryTypeId(),
                    getTransactionLogRequest.getCurrency(),
                    getTransactionLogRequest.getBaseCoin(),
                    getTransactionLogRequest.getTransactionType() == null ? null : getTransactionLogRequest.getTransactionType().getTransactionTypeId(),
                    getTransactionLogRequest.getStartTime(),
                    getTransactionLogRequest.getEndTime(),
                    getTransactionLogRequest.getLimit(),
                    getTransactionLogRequest.getCursor()
            ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void setAccountMarginMode(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        var setMarginMode = converter.mapToSetMarginModeRequest(request);
        bybitApiService.setAccountMarginMode(setMarginMode).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void modifyAccountMMP(AccountDataRequest setMMPRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToSetMMPRequest(setMMPRequest);
        bybitApiService.modifyAccountMMP(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void resetAccountMMP(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        var resetMMPRequest = converter.mapToResetMarginModeRequest(request);
        bybitApiService.resetAccountMMP(resetMMPRequest).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountMMPState(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountMMPState(request.getBaseCoin()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAccountSMPGroup(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAccountSMPGroupId().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void setAccountSpotHedging(AccountDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        var setSpotHedging = converter.mapToSetSpotHedgingModeRequest(request);
        bybitApiService.setAccountSpotHedging(setSpotHedging).enqueue(new BybitApiCallbackAdapter<>(callback));
    }
}

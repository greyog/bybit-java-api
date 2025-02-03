package com.bybit.api.client.impl;

import com.bybit.api.client.restApi.BybitApiAsyncAssetRestClient;
import com.bybit.api.client.restApi.BybitApiCallback;
import com.bybit.api.client.restApi.BybitApiService;
import com.bybit.api.client.domain.asset.request.AssetDataRequest;
import com.bybit.api.client.domain.asset.request.AssetCancelWithdrawRequest;
import com.bybit.api.client.domain.asset.request.SetAssetDepositAccountRequest;
import com.bybit.api.client.service.BybitJsonConverter;

import java.util.HashMap;
import java.util.Map;

import static com.bybit.api.client.service.BybitApiServiceGenerator.createService;

/**
 * Implementation of Bybit REST API using Retrofit with asynchronous/non-blocking method calls.
 */
public class BybitApiAsyncAssetRestClientImpl implements BybitApiAsyncAssetRestClient {

    private final BybitApiService bybitApiService;
    private final BybitJsonConverter converter = new BybitJsonConverter();

    public BybitApiAsyncAssetRestClientImpl(String apiKey, String secret, String baseUrl, boolean debugMode, long recvWindow, String logOption) {
        bybitApiService = createService(BybitApiService.class, apiKey, secret, baseUrl, debugMode, recvWindow, logOption, "");
    }

    // Asset endpoints
    @Override
    public void getAssetCoinExchangeRecords(AssetDataRequest coinExchangeRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetCoinExchangeRecords(
                coinExchangeRecordsRequest.getFromCoin(),
                coinExchangeRecordsRequest.getToCoin(),
                coinExchangeRecordsRequest.getLimit(),
                coinExchangeRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetDeliveryRecords(AssetDataRequest deliveryRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetDeliveryRecords(
                deliveryRecordsRequest.getCategory() == null ? null : deliveryRecordsRequest.getCategory().getCategoryTypeId(),
                deliveryRecordsRequest.getSymbol(),
                deliveryRecordsRequest.getExpDate(),
                deliveryRecordsRequest.getLimit(),
                deliveryRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetUSDCSettlementRecords(AssetDataRequest usdcSettlementRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetUSDCSettlementRecords(
                usdcSettlementRequest.getCategory() == null ? null : usdcSettlementRequest.getCategory().getCategoryTypeId(),
                usdcSettlementRequest.getSymbol(),
                usdcSettlementRequest.getLimit(),
                usdcSettlementRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetInfo(AssetDataRequest assetInfoRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetInfo(
                assetInfoRequest.getAccountType() == null ? null : assetInfoRequest.getAccountType().getAccountTypeValue(),
                assetInfoRequest.getCoin()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetAllCoinsBalance(AssetDataRequest allCoinsBalanceRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetAllCoinsBalance(
                allCoinsBalanceRequest.getAccountType() == null ? null : allCoinsBalanceRequest.getAccountType().getAccountTypeValue(),
                allCoinsBalanceRequest.getMemberId(),
                allCoinsBalanceRequest.getCoin(),
                allCoinsBalanceRequest.getWithBonus() == null ? null : String.valueOf(allCoinsBalanceRequest.getWithBonus().getValue())
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetTransferableCoins(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetTransferableCoins(
                request.getFromAccountType() == null ? null : request.getFromAccountType().getAccountTypeValue(),
                request.getToAccountType() == null ? null : request.getToAccountType().getAccountTypeValue()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetSingleCoinBalance(AssetDataRequest singleCoinBalanceRequest, BybitApiCallback<GenericResponse<?>> callback) {
        converter.getSingleCoinBalance(bybitApiService, singleCoinBalanceRequest).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void createAssetInternalTransfer(AssetDataRequest assetInternalTransferRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToAssetInternalTransferRequest(assetInternalTransferRequest);
        bybitApiService.createAssetInternalTransfer(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetTransferSubUidList(BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetTransferSubUidList().enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void createAssetUniversalTransfer(AssetDataRequest assetUniversalTransferRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToAssetUniversalTransferRequest(assetUniversalTransferRequest);
        bybitApiService.createAssetUniversalTransfer(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetInternalTransferRecords(AssetDataRequest internalTransferRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetInternalTransferRecords(
                internalTransferRequest.getTransferId(),
                internalTransferRequest.getCoin(),
                internalTransferRequest.getTransferStatus() == null ? null : internalTransferRequest.getTransferStatus().getStatus(),
                internalTransferRequest.getStartTime(),
                internalTransferRequest.getEndTime(),
                internalTransferRequest.getLimit(),
                internalTransferRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetUniversalTransferRecords(AssetDataRequest universalTransferRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetUniversalTransferRecords(
                universalTransferRequest.getTransferId(),
                universalTransferRequest.getCoin(),
                universalTransferRequest.getTransferStatus() == null ? null : universalTransferRequest.getTransferStatus().getStatus(),
                universalTransferRequest.getStartTime(),
                universalTransferRequest.getEndTime(),
                universalTransferRequest.getLimit(),
                universalTransferRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetAllowedDepositCoinInfo(AssetDataRequest allowedDepositCoinRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetAllowedDepositCoinInfo(
                allowedDepositCoinRequest.getCoin(),
                allowedDepositCoinRequest.getChain(),
                allowedDepositCoinRequest.getLimit(),
                allowedDepositCoinRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void setAssetDepositAccount(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        SetAssetDepositAccountRequest setAssetDepositAccountRequest = converter.mapToSetDepositAccountRequest(request);
        bybitApiService.setAssetDepositAccount(setAssetDepositAccountRequest).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetDepositRecords(
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetSubMembersDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetSubMembersDepositRecords(
                assetDepositRecordsRequest.getSubMemberId(),
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetInternalDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetInternalDepositRecords(
                assetDepositRecordsRequest.getCoin(),
                assetDepositRecordsRequest.getStartTime(),
                assetDepositRecordsRequest.getEndTime(),
                assetDepositRecordsRequest.getLimit(),
                assetDepositRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetMasterDepositAddress(AssetDataRequest masterDepositRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetMasterDepositAddress(
                masterDepositRequest.getCoin(),
                masterDepositRequest.getChainType()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetSubMemberDepositAddress(AssetDataRequest subDepositRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetSubMemberDepositAddress(
                subDepositRequest.getCoin(),
                subDepositRequest.getChainType(),
                subDepositRequest.getSubMemberId()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetCoinInfo(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetCoinInfo(request.getCoin()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetWithdrawalAmount(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetWithdrawalAmount(request.getCoin()).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getAssetWithdrawalRecords(AssetDataRequest assetWithdrawRecordsRequest, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getAssetWithdrawalRecords(
                assetWithdrawRecordsRequest.getWithdrawID(),
                assetWithdrawRecordsRequest.getCoin(),
                assetWithdrawRecordsRequest.getWithdrawType() == null ? null : assetWithdrawRecordsRequest.getWithdrawType().getValue(),
                assetWithdrawRecordsRequest.getStartTime(),
                assetWithdrawRecordsRequest.getEndTime(),
                assetWithdrawRecordsRequest.getLimit(),
                assetWithdrawRecordsRequest.getCursor()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void cancelAssetWithdraw(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        AssetCancelWithdrawRequest assetCancelWithdrawRequest = converter.mapToAssetCancelWithdrawRequest(request);
        bybitApiService.cancelAssetWithdraw(assetCancelWithdrawRequest).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void createAssetWithdraw(AssetDataRequest assetWithdrawRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToAssetWithdrawRequest(assetWithdrawRequest);
        bybitApiService.createAssetWithdraw(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void requestQuote(AssetDataRequest assetQuoteRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToAssetQuoteRequest(assetQuoteRequest);
        bybitApiService.requestQuote(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void confirmQuote(String quoteTxId, BybitApiCallback<GenericResponse<?>> callback) {
        Map<String, String> map = new HashMap<>();
        map.put("quoteTxId", quoteTxId);
        bybitApiService.confirmQuote(map).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void confirmQuote(AssetDataRequest assetQuoteRequest, BybitApiCallback<GenericResponse<?>> callback) {
        var request = converter.mapToAssetConfirmQuoteRequest(assetQuoteRequest);
        bybitApiService.confirmQuote(request).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getConvertCoinList(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getConvertCoinList(
                request.getCoin(),
                request.getSide(),
                request.getToAccountType() == null ? null : request.getToAccountType().getAccountTypeValue()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getConvertCoinStatus(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getConvertCoinStatus(
                request.getQuoteTxId(),
                request.getToAccountType() == null ? null : request.getToAccountType().getAccountTypeValue()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

    @Override
    public void getConvertCoinHistory(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback) {
        bybitApiService.getConvertCoinHistory(
                request.getToAccountType() == null ? null : request.getToAccountType().getAccountTypeValue(),
                request.getIndex(),
                request.getLimit()
        ).enqueue(new BybitApiCallbackAdapter<>(callback));
    }

}

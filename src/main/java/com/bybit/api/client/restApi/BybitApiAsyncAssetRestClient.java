package com.bybit.api.client.restApi;

import com.bybit.api.client.domain.asset.request.AssetDataRequest;

public interface BybitApiAsyncAssetRestClient {
    // Asset Endpoints
    void getAssetCoinExchangeRecords(AssetDataRequest assetDataRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetDeliveryRecords(AssetDataRequest deliveryReco, BybitApiCallback<GenericResponse<?>> callbackrdsRequest);
    void getAssetUSDCSettlementRecords(AssetDataRequest usdcSettlementRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetInfo(AssetDataRequest assetInfoRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetAllCoinsBalance(AssetDataRequest allCoinsBalanceRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetTransferableCoins(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetSingleCoinBalance(AssetDataRequest singleCoinBalanceRequest, BybitApiCallback<GenericResponse<?>> callback);
    void createAssetInternalTransfer(AssetDataRequest assetInternalTransferRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetTransferSubUidList(BybitApiCallback<GenericResponse<?>> callback);
    void createAssetUniversalTransfer(AssetDataRequest assetUniversalTransferRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetInternalTransferRecords(AssetDataRequest internalTransferRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetUniversalTransferRecords(AssetDataRequest universalTransferRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetAllowedDepositCoinInfo(AssetDataRequest allowedDepositCoinRequest, BybitApiCallback<GenericResponse<?>> callback);
    void setAssetDepositAccount(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetSubMembersDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetInternalDepositRecords(AssetDataRequest assetDepositRecordsRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetMasterDepositAddress(AssetDataRequest masterDepositRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetSubMemberDepositAddress(AssetDataRequest subDepositRequest, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetCoinInfo(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetWithdrawalAmount(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getAssetWithdrawalRecords(AssetDataRequest assetWithdrawRecordsRequest, BybitApiCallback<GenericResponse<?>> callback);
    void cancelAssetWithdraw(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void createAssetWithdraw(AssetDataRequest assetWithdrawRequest, BybitApiCallback<GenericResponse<?>> callback);
    // convert coin endpoints
    void requestQuote(AssetDataRequest assetQuoteRequest,BybitApiCallback<GenericResponse<?>> callback);
    void confirmQuote(String quoteTxId, BybitApiCallback<GenericResponse<?>> callback);

    void confirmQuote(AssetDataRequest assetQuoteRequest, BybitApiCallback<GenericResponse<?>> callback);

    void getConvertCoinList(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getConvertCoinStatus(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
    void getConvertCoinHistory(AssetDataRequest request, BybitApiCallback<GenericResponse<?>> callback);
}
